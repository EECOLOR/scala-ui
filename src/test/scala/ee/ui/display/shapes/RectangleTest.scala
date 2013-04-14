package ee.ui.display.shapes

import org.specs2.mutable.Specification
import ee.ui.display.Node
import ee.ui.display.implementation.contracts.RectangleContract
import ee.ui.display.traits.Fill
import utils.SubtypeTest
import ee.ui.display.Shape

object RectangleTest extends Specification {

  xonly
  
  "Rectangle" should {
    
    "be of the correct type" in {
      
      SubtypeTest[Rectangle <:< Shape with RectangleContract]
    }
  }
  
}