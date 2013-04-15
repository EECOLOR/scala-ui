package ee.ui.primitives.transformation

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.primitives.Transformation
import ee.ui.primitives.TransformationTest

object AffineTest extends Specification {

  xonly

  "Affine" should {

    "extend Transformation" in {
      SubtypeTest[Affine <:< Transformation]
    }

    "have the correct default values" in {
      TransformationTest.checkDefaultValues(Affine())
    }

    "set the correct values" in {
      val affine = Affine(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

      affine.xx === 1
      affine.xy === 2
      affine.xz === 3
      affine.xt === 4

      affine.yx === 5
      affine.yy === 6
      affine.yz === 7
      affine.yt === 8

      affine.zx === 9
      affine.zy === 10
      affine.zz === 11
      affine.zt === 12
    }
  }

}