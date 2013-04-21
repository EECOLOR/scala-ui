package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.display.detail.ReadOnlyNode
import utils.SubtypeTest
import ee.ui.display.detail.ReadOnlyShape
import ee.ui.display.traits.Fill
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.display.traits.Stroke

object ShapeTest extends Specification {

  xonly

  "Shape" should {

    "extends the correct traits" in {
      SubtypeTest[Shape <:< Node with ReadOnlyShape with Fill with Stroke]
    }
    
    "be abstract" in {
      TestUtils.eval("new ee.ui.display.Shape") must throwA[ToolBoxError].like {
        case e => e.getMessage must contain("class Shape is abstract")
      }
    }
  }
}