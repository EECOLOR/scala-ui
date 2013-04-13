package ee.ui.display.implementation.contracts

import org.specs2.mutable.Specification
import utils.TestUtils
import utils.TypeTest
import ee.ui.display.traits.ReadOnlySize
import utils.SubtypeTest
import scala.tools.reflect.ToolBoxError
import ee.ui.display.traits.ReadOnlyFill
import ee.ui.display.shapes.detail.ReadOnlyRectangle

object NodeContractTest extends Specification {

  xonly

  "NodeContract" should {
    "be a sealed trait" in {
      TestUtils.eval(
        """|import ee.ui.display.implementation.contracts.NodeContract
           |new NodeContract {}              
           |""".stripMargin) should throwA[ToolBoxError].like {
          case e => e.getMessage must contain("illegal inheritance from sealed trait NodeContract")
        }
    }
    "have sub traits with the correct types" in {
      SubtypeTest[RectangleContract <:< NodeContract with ReadOnlyRectangle]
    }
  }
  
}