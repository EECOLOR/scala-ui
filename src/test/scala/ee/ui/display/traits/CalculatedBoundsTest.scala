package ee.ui.display.traits

import org.specs2.mutable.Specification

import ee.ui.members.ReadOnlyProperty
import ee.ui.primitives.Bounds
import utils.SignatureTest
import utils.SubtypeTest

class CalculatedBoundsTest extends Specification {

  xonly
  isolated

  "CalculatedBounds" should {

    val calculatedBounds = new CalculatedBounds with Size with Position with Rotation {}
    def bounds = calculatedBounds.bounds

    "extends CalculatedTransformation" in {
      SubtypeTest[CalculatedBounds <:< CalculatedTransformation]
    }

    "have a property bounds" in {
      SignatureTest[CalculatedBounds, ReadOnlyProperty[Bounds]](_.bounds)
    }

    "be ZERO by default" in {
      bounds.value === Bounds.ZERO
    }

    "reflect 0, 0, width, height" in {
      calculatedBounds.width = 1
      calculatedBounds.height = 1

      bounds.value === Bounds(0, 0, 1, 1)
    }

    "be transformed by totalTransformation" in {
      calculatedBounds.width = 40
      calculatedBounds.height = 20
      calculatedBounds.rotation = 90
      calculatedBounds.x = 10
      calculatedBounds.y = 20
      
      bounds.value === Bounds(20, 10, 40, 50)
    }
  }

}