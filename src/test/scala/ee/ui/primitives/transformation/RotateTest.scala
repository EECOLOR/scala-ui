package ee.ui.primitives.transformation

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.primitives.Transformation
import ee.ui.primitives.Point

object RotateTest extends Specification {

  xonly

  "Rotate" should {

    "extend Transformation" in {
      SubtypeTest[Rotate <:< Transformation]
    }
    
    "have the correct default values" in {
      val Rotate(angle, axis, pivot) = Rotate()
      angle === 0
      axis === Point(0, 0, 1)
      pivot === Point(0, 0, 0)
    }

    "have X, Y and Z_AXIS properties" in {
      Rotate.X_AXIS === Point(1, 0, 0)
      Rotate.Y_AXIS === Point(0, 1, 0)
      Rotate.Z_AXIS === Point(0, 0, 1)
    }

    "start at identity" in {
      Rotate().isIdentity
    }
    
    "apply the rotation correctly" in {
      Rotate(1, Point(2, 3, 4), Point(5, 6, 7)).toAffine === Affine(0.9998687027210269,-0.012931810714545345,0.00976450667539553,0.009895803954368176,0.012994833408452405,0.9998949621768216,-0.006418638336842377,-0.019413471745295396,-0.009680476416852782,0.006544683724656498,0.999931725414934,0.009612201831786571)
    }
    
    "throw an error if the axis is invalid" in {
      Rotate(axis = Point(0, 0, 0)) should throwAn[IllegalArgumentException]
    }
  }

}