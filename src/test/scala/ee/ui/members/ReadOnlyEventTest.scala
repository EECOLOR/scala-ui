package ee.ui.members

import org.specs2.mutable.Specification
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.system.RestrictedAccess
import utils.SignatureTest
import ee.ui.members.detail.Subscription

class ReadOnlyEventTest extends Specification {
  xonly
  isolated

  val event = ReadOnlyEvent[Int]()

  def fire[T](event: ReadOnlyEvent[T], value: T): Unit = ReadOnlyEvent.fire(event, value)(RestrictedAccess)
  def fire(value: Int): Unit = fire(event, value)
  def fireOne = fire(1)
  var result = 0

  "ReadOnlyEvent" should {
    "have the ability to observe" in {
      SignatureTest[ReadOnlyEvent[Int], Subscription](_.observe(observer = { information: Int => }))
    }

    "have a simpler method of observe" in {
      SignatureTest[ReadOnlyEvent[Int], Subscription](_.apply(observer = { information: Int => }))
    }

    "not be able to fire directly" in {
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

    "be fired using a detour" in {
      event { result = _ }

      ReadOnlyEvent.fire(event, 1)(RestrictedAccess)

      result === 1
    }

    "have the ability to unsubscribe an observer" in {
      val subscription = event { result = _ }
      subscription.unsubscribe()
      fireOne

      result === 0
    }

    "have the ability to collect events" in {
      var caughtInformation = "0"
      val newEvent: ReadOnlyEvent[String] =
        event collect {
          case information if (information == 1) => information.toString
        }

      newEvent { caughtInformation = _ }

      fire(1)
      fire(2)

      caughtInformation === "1"
    }

    "firing the on a collected event throw an exception" in {
      val newEvent = event collect {
        case _ =>
      }

      fire(newEvent, {}) must throwA[UnsupportedOperationException]
    }

    "have the ability to map events" in {
      val newEvent: ReadOnlyEvent[String] =
        event map (_.toString)

      var caughtInformation = "0"

      newEvent { caughtInformation = _ }

      fire(1)

      caughtInformation === "1"
    }

    "have the ability to filter events" in {
      val newEvent: ReadOnlyEvent[Int] =
        event filter (_ > 1)

      var caughtInformation = 0

      newEvent { caughtInformation = _ }

      fire(1)
      fire(2)

      caughtInformation === 2
    }

    "be able to convert to a signal" in {
      val signal:ReadOnlySignal = event
      ok
    }

  }
}