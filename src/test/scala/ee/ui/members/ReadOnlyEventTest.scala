package ee.ui.members

import org.specs2.mutable.Specification
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.system.RestrictedAccess

class ReadOnlyEventTest extends Specification {
  xonly
  isolated
  
  val event = ReadOnlyEvent[Int]()
  
  def fireOne = ReadOnlyEvent.fire(event, 1)(RestrictedAccess)
  var result = 0
  
  "ReadOnlyEvent" should {
    "have the ability to observe" in {
      event.observe { information => }
      ok
    }
    
    "have a simpler method of observe" in {
      event { information => }
      ok
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
          e.getMessage must contain("method fire in trait ReadOnlyEvent cannot be accessed in ee.ui.members.ReadOnlyEvent[Int]")
      }
    }
    
    "be fired using a detour" in {
      event { result = _}
      fireOne
      
      result === 1
    }
    
    "have the ability to unsubscribe an observer" in {
      val subscription = event { result = _ }
      subscription.unsubscribe()
      fireOne
      
      result === 0
    }
  }
}