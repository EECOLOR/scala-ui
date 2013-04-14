package ee.ui.system

import scala.annotation.implicitNotFound
import scala.tools.reflect.ToolBoxError

import org.specs2.mutable.Specification

import utils.SubtypeTest
import utils.TestUtils

object AccessRestrictionTest extends Specification {
  
  xonly
  
  "AccessRestriction" should {
    
    "be available as object" in {
      SubtypeTest[RestrictedAccess.type <:< AccessRestriction]
    }
    
    "give a message as to what is meant if not implicitly available" in {
      
      def result = TestUtils.eval(
          """|import ee.ui.system.AccessRestriction
             |implicitly[AccessRestriction]
             |""".stripMargin)
             
      result must throwA[ToolBoxError].like {
        case (e) =>
          e.getMessage must contain ("This is not allowed for normal use. If you however know what you are doing you can use the RestrictedAccess object.")
      }
    }
  }
}