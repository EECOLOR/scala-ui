package ee.ui.members

import org.specs2.mutable.Specification
import utils.SignatureTest
import ee.ui.members.detail.Subscription
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.system.RestrictedAccess

class ReadOnlySignalTest extends Specification {
  xonly
  isolated

  val signal = ReadOnlySignal()
  var fired = false
  def fire() = ReadOnlySignal.fire(signal)(RestrictedAccess)
  
  "ReadOnlySignal" should {
    "have an observe method" in {
      SignatureTest[ReadOnlySignal, Subscription](_.observe(observer = { /* I am an observer */ }))
    }
    
    "have a simpler method of observe" in {
      SignatureTest[ReadOnlySignal, Subscription](_.apply(observer = { /* I am an observer */ }))
    }
    
    "have an easy constructor" in {
      val r: ReadOnlySignal = ReadOnlySignal()
      ok
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

    "be fired using a detour" in {

      signal { fired = true }

      ReadOnlySignal.fire(signal)(RestrictedAccess)

      fired

    }

    "have the ability to unsubscribe an observer" in {
      
      val subscription = signal { fired = true }
      subscription.unsubscribe()
      fire

      !fired
    }
  }
}