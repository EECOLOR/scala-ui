package ee.ui.display.implementation.contracts

import org.specs2.mutable.Specification
import utils.TestUtils
import utils.TypeTest
import ee.ui.display.traits.ReadOnlySize
import utils.SubtypeTest
import scala.tools.reflect.ToolBoxError
import ee.ui.display.traits.ReadOnlyFill
import ee.ui.display.shapes.detail.ReadOnlyRectangle
import utils.SignatureTest
import ee.ui.display.detail.ReadOnlyShape
import ee.ui.display.shapes.detail.ReadOnlyText

object NodeContractTest extends Specification {

  xonly

  "NodeContract" should {

    "be a sealed trait" in {

      TestUtils.eval("new ee.ui.display.implementation.contracts.NodeContract {}") should throwA[ToolBoxError].like {
        case e => e.getMessage must contain("illegal inheritance from sealed trait NodeContract")
      }
    }

    "have sub traits with the correct types" in {
      SubtypeTest[RectangleContract <:< NodeContract with ShapeContract with ReadOnlyRectangle]
      SubtypeTest[TextContract <:< NodeContract with ShapeContract with ReadOnlyText]
    }
  }

  "ShapeContract" should {

    "be a sealed trait" in {
      TestUtils.eval("new ee.ui.display.implementation.contracts.ShapeContract {}") should throwA[ToolBoxError].like {
        case e => e.getMessage must contain("illegal inheritance from sealed trait ShapeContract")
      }
    }

    "extend the correct types" in {
      SubtypeTest[ShapeContract <:< ReadOnlyShape]
    }

    "have an asNodeContract property" in {
      SignatureTest[ShapeContract, NodeContract](_.asNodeContract)
    }

  }

}