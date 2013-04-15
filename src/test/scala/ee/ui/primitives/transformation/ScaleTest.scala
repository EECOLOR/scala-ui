package ee.ui.primitives.transformation

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.primitives.Transformation
import ee.ui.primitives.Point
import ee.ui.primitives.Bounds

object ScaleTest extends Specification {

  // has changed values for the values that are not touched by scale
  def invalidScale(x: Double = 1, y: Double = 1, z: Double = 1, pivot: Point = Point(0, 0, 0)) = new Scale(x, y, z, pivot) {
    override val xy: Double = 1
    override val xz: Double = 1

    override val yx: Double = 1
    override val yz: Double = 1

    override val zx: Double = 1
    override val zy: Double = 1
  }

  "Scale" should {

    "extend Transformation" in {
      SubtypeTest[Scale <:< Transformation]
    }

    "have the correct default values" in {
      val Scale(x, y, z, pivot) = Scale()
      x === 1
      y === 1
      z === 1
      pivot === Point.ZERO
    }

    "start at identity" in {
      Scale().isIdentity
    }

    "have the correct values" in {
      Scale(1, 2, 3, Point(4, 5, 6)).toAffine === Affine(xx = 1, yy = 2, zz = 3, xt = 0, yt = -5, zt = -12)
    }

    "overrides isPureTranslation" in {
      invalidScale().isPureTranslation
    }

    "overrides isIdentity" in {
      invalidScale().isIdentity
    }

    "overrides transform" in {
      (invalidScale(1, 2, 3, Point(4, 5, 6)) transform Point(1, 2, 3)) === Point(1, -1, -3)
      (invalidScale(1, 2, 3, Point(4, 5, 6)) transform Bounds(1, 2, 3, 4, 5, 6)) === Bounds(1, -1, -3, 4, 5, 6)
    }

    "overrides ++" in {
      (invalidScale(1, 2, 3, Point(4, 5, 6)) ++ Affine(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)) === Affine(1.0,2.0,3.0,8.0,10.0,12.0,14.0,24.0,27.0,30.0,33.0,48.0)
    }
  }

}