package ee.ui.display.shapes

import org.specs2.mutable.Specification
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.traits.Fill
import ee.ui.display.Node
import utils.SubtypeTest
import ee.ui.display.implementation.contracts.RectangleContract

object RectangleTest extends Specification {

  xonly
  
  "Rectangle" should {
    "be of the correct type" in {
      SubtypeTest[Rectangle <:< Node with RectangleContract with Fill]
    }
  }
  
}