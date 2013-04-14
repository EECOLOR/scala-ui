package ee.ui.members

import scala.collection.mutable.ListBuffer
import scala.tools.reflect.ToolBoxError

import org.specs2.mutable.Specification

import ee.ui.members.detail.Subscription
import ee.ui.system.RestrictedAccess
import utils.SignatureTest
import utils.TestUtils

class ReadOnlySignalTest extends Specification {

  xonly
  isolated

  val signal = ReadOnlySignal()
  def fire(): Unit = fire(signal)
  def fire(signal: ReadOnlySignal) = ReadOnlySignal.fire(signal)(RestrictedAccess)

  val signal1 = ReadOnlySignal()
  val signal2 = ReadOnlySignal()
  val combined: ReadOnlySignal = signal1 | signal2

  "ReadOnlySignal" should {

    "have an observe method" in {
      SignatureTest[ReadOnlySignal, Unit, Subscription](_ observe _)
    }

    "have a simpler method of observe" in {
      SignatureTest[ReadOnlySignal, Unit, Subscription](_ apply _)
    }

    "not be able to fire directly" in {
      def result = TestUtils.eval("""
          |import ee.ui.members.ReadOnlySignal
          |  
          |val myObj = new {
          |  val signal = ReadOnlySignal()
          |}
          |
          |// will not compile:
          |myObj.signal.fire()
        """.stripMargin)

      result must throwA[ToolBoxError].like {
        case e =>
          e.getMessage must contain("method fire in trait ReadOnlySignal cannot be accessed in ee.ui.members.ReadOnlySignal")
      }
    }

    "have the ability to be combined" in {

      var fireCount = 0
      combined {
        fireCount += 1
      }
      fire(signal1)
      fire(signal2)

      fireCount === 2
    }

    "throw an error if fire is called on a combined signal" in {
      fire(combined) must throwAn[UnsupportedOperationException]
    }

    "should be able to enable, disable and unsubscribe" in {
      var eventCount = 0

      def fire(signal: ReadOnlySignal) = {
        this.fire(signal)
        eventCount += 1
      }

      val results = ListBuffer.empty[Int]
      val subscription = combined { results += eventCount }
      subscription.disable()
      fire(signal1)
      fire(signal2)
      subscription.enable()
      fire(signal1)
      fire(signal2)
      subscription.unsubscribe()
      fire(signal1)
      fire(signal2)

      results.toSeq === Seq(2, 3)
    }

    "have an apply method" in {
      SignatureTest[ReadOnlySignal.type, ReadOnlySignal](_.apply)
      ReadOnlySignal() must beAnInstanceOf[Signal]
    }

    "be fired using a detour" in {
      var fired = false

      signal { fired = true }

      ReadOnlySignal.fire(signal)(RestrictedAccess)

      fired
    }
  }
}