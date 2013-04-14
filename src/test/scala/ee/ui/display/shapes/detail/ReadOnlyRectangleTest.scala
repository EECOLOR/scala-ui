package ee.ui.display.shapes.detail

import org.specs2.mutable.Specification
import ee.ui.display.detail.ReadOnlyNode
import ee.ui.display.detail.ReadOnlyShape
import ee.ui.display.traits.ReadOnlyFill
import utils.SubtypeTest

object ReadOnlyRectangleTest extends Specification {
  
  xonly
  
  "ReadOnlyRectangle" should {
    
    "extend the correct traits" in {
      SubtypeTest[ReadOnlyRectangle <:< ReadOnlyShape]
    }
  }
}