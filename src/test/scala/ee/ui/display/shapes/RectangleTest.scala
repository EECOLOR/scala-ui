package ee.ui.display.shapes

import org.specs2.mutable.Specification
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.traits.Fill
import ee.ui.display.detail.ReadOnlyRectangle
import ee.ui.display.Node

object RectangleTest extends Specification {

  xonly
  
  "Rectangle" should {
    "extend ReadOnlySize" in {
      new Rectangle must beAnInstanceOf[Node with ReadOnlyRectangle with Fill]
    }
  }
  
}