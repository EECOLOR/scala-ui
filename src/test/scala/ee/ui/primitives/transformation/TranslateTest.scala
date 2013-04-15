package ee.ui.primitives.transformation

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.primitives.Transformation
import ee.ui.primitives.Point
import ee.ui.primitives.Bounds

object TranslateTest extends Specification {

  xonly

  // has changed values for the values that are not touched by shear
  def invalidTranslate(x: Double = 0, y: Double = 0, z: Double = 0) = new Translate(x, y, z) {
    override val xx: Double = 1
    override val xy: Double = 1
    override val xz: Double = 1

    override val yx: Double = 1
    override val yy: Double = 1
    override val yz: Double = 1

    override val zx: Double = 1
    override val zy: Double = 1
    override val zz: Double = 1
  }

  "Translate" should {

    "extend Transformation" in {
      SubtypeTest[Translate <:< Transformation]
    }

    "have the correct default values" in {
      val Translate(x, y, z) = Translate()
      x === 0
      y === 0
      z === 0
    }

    "start at identity" in {
      Translate().isIdentity
    }

    "have the correct values" in {
      Translate(1, 2, 3).toAffine === Affine(xt = 1, yt = 2, zt = 3)
    }

    "overrides isPureTranslation" in {
      invalidTranslate().isPureTranslation
    }

    "overrides isIdentity" in {
      invalidTranslate().isIdentity
    }

    "overrides transform" in {
      (invalidTranslate(1, 2, 3) transform Point(1, 2, 3)) === Point(2.0, 4.0, 6.0)
      (invalidTranslate(1, 2, 4) transform Bounds(1, 2, 3, 4, 5, 6)) === Bounds(2.0, 4.0, 7.0, 5.0, 7.0, 10.0)
    }

    "overrides ++" in {
      (invalidTranslate(1, 2, 3) ++ Affine(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)) === Affine(1.0,2.0,3.0,18.0,5.0,6.0,7.0,46.0,9.0,10.0,11.0,74.0)
    }
  }

}