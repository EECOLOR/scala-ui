package ee.ui.display.detail

import org.specs2.mutable.Specification
import utils.TestUtils
import utils.TypeTest
import ee.ui.display.traits.ReadOnlySize
import utils.SubtypeTest
import scala.tools.reflect.ToolBoxError
import ee.ui.display.traits.ReadOnlyFill

object NodeContractTest extends Specification {

  xonly

  "NodeContract" should {
    "be a sealed trait" in {
      TestUtils.eval(
        """|import ee.ui.display.detail.NodeContract
           |new NodeContract {}              
           |""".stripMargin) should throwA[ToolBoxError].like {
          case e => e.getMessage must contain("illegal inheritance from sealed trait NodeContract")
        }
    }
  }

  "ReadOnlyNode" should {
    "extend the correct traits" in {
      SubtypeTest[ReadOnlyNode <:< ReadOnlySize]
    }
  }
  
  "ReadOnlyRectangle" should {
    "extend the correct traits" in {
      SubtypeTest[ReadOnlyRectangle <:< ReadOnlyNode with ReadOnlyFill]
    }
  }

}