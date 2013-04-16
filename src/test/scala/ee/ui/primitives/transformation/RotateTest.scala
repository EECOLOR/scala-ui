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
      Rotate(1, Point(2, 3, 4), Point(5, 6, 7)).toAffine === Affine(0.9998687027210269, -0.012931810714545345, 0.00976450667539553, 0.009895803954368176, 0.012994833408452405, 0.9998949621768216, -0.006418638336842377, -0.019413471745295396, -0.009680476416852782, 0.006544683724656498, 0.999931725414934, 0.009612201831786571)
    }

    "throw an error if the axis is invalid" in {
      Rotate(axis = Point(0, 0, 0)) should throwAn[IllegalArgumentException]
    }

    "apply rotation correctly in another test case" in {

      val r = Rotate(45, Point(20, 30, 40), Point(66, 77, 88))

      val (x, y, z) = (20.0 / Math.sqrt(2900.0), 30.0 / Math.sqrt(2900.0), 40.0 / Math.sqrt(2900.0))

      val sin = Math.sin(Math.PI / 4)
      val cos = Math.cos(Math.PI / 4)
      val `1 - cos` = 1 - cos

      r.toAffine === Affine(
        cos + x * x * `1 - cos`, //xx
        x * y * `1 - cos` - z * sin, //xy
        x * z * `1 - cos` + y * sin, //xz
        66
          - 66 * (cos + x * x * `1 - cos`)
          - 77 * (x * y * `1 - cos` - z * sin)
          - 88 * (x * z * `1 - cos` + y * sin), //xt
        y * x * `1 - cos` + z * sin, //yx
        cos + y * y * `1 - cos`, //yy
        y * z * `1 - cos` - x * sin, //yz
        77
          - 66 * (y * x * `1 - cos` + z * sin)
          - 77 * (cos + y * y * `1 - cos`)
          - 88 * (y * z * `1 - cos` - x * sin), //yt
        z * x * `1 - cos` - y * sin, //zx
        z * y * `1 - cos` + x * sin, //zy
        cos + z * z * `1 - cos`, //zz
        88
          - 66 * (z * x * `1 - cos` - y * sin)
          - 77 * (z * y * `1 - cos` + x * sin)
          - 88 * (cos + z * z * `1 - cos`) //zt
          )
    }
  }

}