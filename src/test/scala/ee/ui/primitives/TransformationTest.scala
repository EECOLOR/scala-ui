package ee.ui.primitives

import org.specs2.mutable.Specification

import ee.ui.primitives.transformation.Affine
import ee.ui.primitives.transformation.Identity
import utils.SignatureTest

object TransformationTest extends Specification {

  xonly

  val transformation:Transformation = Affine(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
  
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

      val Point(x, y, z) = point
      val Affine(xx, xy, xz, xt, yx, yy, yz, yt, zx, zy, zz, zt) = transformation

      Point(18, 46, 74) === (transformation transform point)
    }

    "be able to transform bounds" in {
      SignatureTest[Transformation, Bounds, Bounds](_ transform _)

      val bounds = Bounds(-1, -2, -3, 1, 2, 3)

      Bounds(-10, -30, -50, 18, 46, 74) === (transformation transform bounds)
    }

    "be able to concat with another transformation" in {
      val affine1:Transformation = Affine(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24)
      val affine2 = transformation

      affine1 ++ affine2 === Affine(76.0, 88.0, 100.0, 116.0, 196.0, 232.0, 268.0, 312.0, 316.0, 376.0, 436.0, 508.0)
    }

    "be able to convert to affine" in {
      
      val t1 = new Transformation {
        override val xx: Double = 1
        override val xy: Double = 2
        override val xz: Double = 3
        override val xt: Double = 4

        override val yx: Double = 5
        override val yy: Double = 6
        override val yz: Double = 7
        override val yt: Double = 8

        override val zx: Double = 9
        override val zy: Double = 10
        override val zz: Double = 11
        override val zt: Double = 12
      }

      t1.toAffine === transformation
    }
    
    "not convert when it already is affine" in {
      val t2: Transformation = transformation
      t2.toAffine eq transformation
    }
    
    "have a ZERO property that is Identity" in {
      SignatureTest[Transformation.type, Transformation](_.ZERO)
      Transformation.ZERO === Identity
    }
  }
}