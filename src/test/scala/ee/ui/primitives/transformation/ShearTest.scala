package ee.ui.primitives.transformation

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.primitives.Transformation
import ee.ui.primitives.Point
import ee.ui.primitives.Bounds

object ShearTest extends Specification {

  xonly
  
  // has changed values for the values that are not touched by shear
  def invalidShear(x: Double = 0, y: Double = 0, pivot: Point = Point(0, 0, 0)) = new Shear(x, y, pivot) {
    override val xx: Double = 2
    override val xz: Double = 1

    override val yy: Double = 2
    override val yz: Double = 1

    override val zx: Double = 1
    override val zy: Double = 1
    override val zz: Double = 2
    override val zt: Double = 1
  }

  "Shear" should {

    "extend Transformation" in {
      SubtypeTest[Shear <:< Transformation]
    }

    "have the correct default values" in {
      val Shear(x, y, pivot) = Shear()
      x === 0
      y === 0
      pivot === Point.ZERO
    }

    "start at identity" in {
      Shear().isIdentity
    }

    "have the correct values" in {
      Shear(1, 2, Point(3, 4)).toAffine === Affine(xy = 1, yx = 2, xt = -4, yt = -6)
    }

    "overrides isPureTranslation" in {
      invalidShear().isPureTranslation
    }

    "overrides isIdentity" in {
      invalidShear().isIdentity
    }

    "overrides transform" in {
      (invalidShear(1, 2, Point(3, 4)) transform Point(1, 2, 3)) === Point(-1.0,-2.0,3.0)
      (invalidShear(1, 2, Point(3, 4)) transform Bounds(1, 2, 3, 4, 5, 6)) === Bounds(-1.0,-2.0,3.0,5.0,7.0,6.0)
    }

    "overrides ++" in {
      (invalidShear(1, 2, Point(3, 4)) ++ Affine(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)) === Affine(5.0,3.0,3.0,-12.0,17.0,11.0,7.0,-48.0,29.0,19.0,11.0,-84.0)
    }
  }

}