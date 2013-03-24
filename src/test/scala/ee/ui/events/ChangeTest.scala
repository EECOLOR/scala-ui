package ee.ui.events

import org.specs2.mutable.Specification
import utils.TestUtils
import scala.tools.reflect.ToolBoxError

class ChangeTest extends Specification {

  xonly
  isolated
  
  "Change" should {
    "be sealed" in {
      def result = TestUtils.eval(
          """|import ee.ui.events.Change
             |new Change[Int] {}""".stripMargin)
             
      result must throwA[ToolBoxError].like {
        case e => e.getMessage must contain("illegal inheritance from sealed trait Change")
      }
    }
    "have a remove subclass" in {
      Remove(1, 1)
      ok
    }
    "have a remove subclass" in {
      Add(1, 1)
      ok
    }
    "have a clear subclass" in {
      Clear(Seq(1, 2))
      ok
    }
  }
  
}