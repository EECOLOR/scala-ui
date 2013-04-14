package ee.ui.members

import scala.collection.mutable.ListBuffer
import scala.tools.reflect.ToolBoxError

import org.specs2.mutable.Specification

import ee.ui.members.detail.BindingSource
import ee.ui.members.detail.Subscription
import ee.ui.system.RestrictedAccess
import utils.SignatureTest
import utils.TestUtils

class ReadOnlyEventTest extends Specification {

  xonly
  isolated

  val event = ReadOnlyEvent[Int]()

  def fire[T](event: ReadOnlyEvent[T], value: T): Unit = ReadOnlyEvent.fire(event, value)(RestrictedAccess)
  def fire(value: Int): Unit = fire(event, value)
  def fireOne = fire(1)
  var result = 0

  val collected: ReadOnlyEvent[String] =
    event collect {
      case information if (information == 1) => information.toString
    }

  val mapped: ReadOnlyEvent[String] = event map (_.toString)

  val filtered: ReadOnlyEvent[Int] = event filter (_ > 1)

  val event1 = ReadOnlyEvent[Int]
  val event2 = ReadOnlyEvent[Long]
  val event3 = ReadOnlyEvent[Char]

  val combined = event1 | event2 | event3

  "ReadOnlyEvent" should {

    "have the ability to observe" in {
      SignatureTest[ReadOnlyEvent[Int], Int => Unit, Subscription](_ observe _)
    }

    "have a simpler method of observe" in {
      SignatureTest[ReadOnlyEvent[Int], Int => Unit, Subscription](_ apply _)
    }

    "not be able to be fired directly" in {
      def result = TestUtils.eval("""
          |import ee.ui.members.ReadOnlyEvent
          |  
          |val myObj = new {
          |  val prop = ReadOnlyEvent[Int]()
          |}
          |
          |// will not compile:
          |myObj.prop.fire(1)
        """.stripMargin)

      result must throwA[ToolBoxError].like {
        case e =>
          // Note that an implicit conversion to signal kicks in before the error is generated
          e.getMessage must contain("method fire in trait ReadOnlySignal cannot be accessed in ee.ui.members.ReadOnlySignal")
      }
    }

    "have the ability to collect events" in {

      var caughtInformation = "0"
      collected { caughtInformation = _ }

      fire(1)
      fire(2)

      caughtInformation === "1"
    }

    "have the ability to map events" in {

      var caughtInformation = "0"

      mapped { caughtInformation = _ }

      fire(1)

      caughtInformation === "1"
    }

    "have the ability to filter events" in {

      var caughtInformation = 0

      filtered { caughtInformation = _ }

      fire(1)
      fire(2)

      caughtInformation === 2
    }

    "have the ability to be combined" in {

      val events = ListBuffer.empty[AnyVal]

      combined { events += _ }

      fire(event1, 1)
      fire(event2, 2l)
      fire(event3, '3')

      events.toSeq === Seq(1, 2l, '3')
    }

    "throw an error for each type of event" in {
      fire(collected, "1") must throwAn[UnsupportedOperationException]
      fire(mapped, "1") must throwAn[UnsupportedOperationException]
      fire(filtered, 1) must throwAn[UnsupportedOperationException]
      fire(combined, 1) must throwAn[UnsupportedOperationException]
    }

    "have an apply method" in {
      SignatureTest[ReadOnlyEvent.type, ReadOnlyEvent[Int]](_.apply[Int])
    }
    
    "be fired using a detour" in {
      event { result = _ }

      ReadOnlyEvent.fire(event, 1)(RestrictedAccess)

      result === 1
    }

    "be able to convert to a binding source" in {
      val b:BindingSource[Option[Int]] = event
      ok
    }
    
    "be able to convert to a signal that can not be fired" in {
      val signal: ReadOnlySignal = event
      ReadOnlySignal.fire(signal)(RestrictedAccess) must throwAn[UnsupportedOperationException]
    }
  }
}