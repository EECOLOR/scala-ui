package ee.ui.primitives

import org.specs2.mutable.Specification
import utils.SubtypeTest
import utils.SignatureTest
import ee.ui.primitives.transformation.Affine

object TransformationTest extends Specification {

  xonly

  def checkDefaultValues(transformation: Transformation) = {
    transformation.xx === 1
    transformation.xy === 0
    transformation.xz === 0
    transformation.xt === 0

    transformation.yx === 0
    transformation.yy === 1
    transformation.yz === 0
    transformation.yt === 0

    transformation.zx === 0
    transformation.zy === 0
    transformation.zz === 1
    transformation.zt === 0
  }

  "Transformation" should {

    "have the correct properties with the correct default values" in {

      val signatureTest = SignatureTest.apply[Transformation, Double] _

      signatureTest(_.xx)
      signatureTest(_.xy)
      signatureTest(_.xz)
      signatureTest(_.xt)

      signatureTest(_.yx)
      signatureTest(_.yy)
      signatureTest(_.yz)
      signatureTest(_.yt)

      signatureTest(_.zx)
      signatureTest(_.zy)
      signatureTest(_.zz)
      signatureTest(_.zt)

      val transformation = new Transformation {}

      checkDefaultValues(transformation)
    }

    "have an isPureTranslation property" in {
      SignatureTest[Transformation, Boolean](_.isPureTranslation)

      Affine(xx = 0).isPureTranslation === false
      Affine(xy = 1).isPureTranslation === false
      Affine(xz = 1).isPureTranslation === false
      Affine(xt = 1).isPureTranslation === true

      Affine(yx = 1).isPureTranslation === false
      Affine(yy = 0).isPureTranslation === false
      Affine(yz = 1).isPureTranslation === false
      Affine(yt = 1).isPureTranslation === true

      Affine(zx = 1).isPureTranslation === false
      Affine(zy = 1).isPureTranslation === false
      Affine(zz = 0).isPureTranslation === false
      Affine(zt = 1).isPureTranslation === true
    }

    "have an isIdentity property" in {
      SignatureTest[Transformation, Boolean](_.isIdentity)

      Affine(xx = 0).isIdentity === false
      Affine(xy = 1).isIdentity === false
      Affine(xz = 1).isIdentity === false
      Affine(xt = 1).isIdentity === false

      Affine(yx = 1).isIdentity === false
      Affine(yy = 0).isIdentity === false
      Affine(yz = 1).isIdentity === false
      Affine(yt = 1).isIdentity === false

      Affine(zx = 1).isIdentity === false
      Affine(zy = 1).isIdentity === false
      Affine(zz = 0).isIdentity === false
      Affine(zt = 1).isIdentity === false
      Affine().isIdentity === true
    }

    "be able to transform a point" in {
      SignatureTest[Transformation, Point, Point](_ transform _)

      val point = Point(1, 2, 3)
      val affine = Affine(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

      val Point(x, y, z) = point
      val Affine(xx, xy, xz, xt, yx, yy, yz, yt, zx, zy, zz, zt) = affine

      Point(
        xx * x + xy * y + xz * z + xt,
        yx * x + yy * y + yz * z + yt,
        zx * x + zy * y + zz * z + zt) === (affine transform point)
    }
  }
}