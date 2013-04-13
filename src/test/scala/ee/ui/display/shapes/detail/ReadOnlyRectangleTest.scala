package ee.ui.display.shapes.detail

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.display.detail.ReadOnlyNode
import ee.ui.display.traits.ReadOnlyFill

object ReadOnlyRectangleTest extends Specification {
  "ReadOnlyRectangle" should {
    "extend the correct traits" in {
      SubtypeTest[ReadOnlyRectangle <:< ReadOnlyNode with ReadOnlyFill]
    }
  }
}