package ee.ui.events

import scala.tools.reflect.ToolBoxError

import org.specs2.mutable.Specification

import utils.SubtypeTest
import utils.TestUtils

object ChangeTest extends Specification {

  xonly
  
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
      SubtypeTest[Remove[_] <:< Change[_]]
    }
    
    "have an add subclass" in {
      SubtypeTest[Add[_] <:< Change[_]]
    }
    
    "have a clear subclass" in {
      SubtypeTest[Clear[_] <:< Change[_]]
    }
  }
  
}