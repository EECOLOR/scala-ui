package ee.ui.display.traits

import org.specs2.mutable.Specification
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import utils.SignatureTest
import ee.ui.members.ReadOnlyProperty
import ee.ui.primitives.Transformation
import ee.ui.primitives.transformation.Affine
import utils.SubtypeTest

class CalculatedTransformationTests extends Specification {

  xonly
  isolated

  class TestCalculatedTransformation extends CalculatedTransformation with Position with Size
    with Translation with Scaling with Rotation with Transformations

  "CalculatedTransformation" should {
    val instance = new TestCalculatedTransformation()
    val totalTransformation = instance.totalTransformation
    def result = totalTransformation.toAffine

    "extend the correct types" in {
      SubtypeTest[CalculatedTransformation <:< ReadOnlyPosition with ReadOnlySize with ReadOnlyTranslation with ReadOnlyScaling with ReadOnlyRotation with ReadOnlyTransformations]
    }

    "have a property totalTransformation" in {
      SignatureTest[CalculatedTransformation, ReadOnlyProperty[Transformation]](_.totalTransformation)
    }

    "be at identity by default" in {
      totalTransformation.isIdentity
    }

    "react to position changes" in {
      instance.x = 1
      result === Affine(xt = 1)
      instance.y = 2
      result === Affine(xt = 1, yt = 2)
      instance.translateX = 3
      result === Affine(xt = 4, yt = 2)
      instance.translateY = 4
      result === Affine(xt = 4, yt = 6)
      instance.translateZ = 5
      result === Affine(xt = 4, yt = 6, zt = 5)
    }

    "react to scaling changes" in {
      instance.width = 40
      instance.height = 20
      result.isIdentity === true
      instance.scaleX = 0.5
      result === Affine(xt = 10, xx = 0.5)
      instance.scaleX = 2
      result === Affine(xt = -20, xx = 2)
      instance.scaleY = 0.5
      result === Affine(xt = -20, xx = 2, yt = 5, yy = 0.5)
      instance.scaleZ = 0.5
      result === Affine(xt = -20, xx = 2, yt = 5, yy = 0.5, zz = 0.5)
    }

    "react to combinations of position and scaling changes" in {
      instance.width = 40
      instance.height = 20
      instance.scaleX = 2
      instance.scaleY = 0.5
      instance.scaleZ = 0.5
      instance.x = 10
      instance.y = 20
      instance.translateX = 5
      instance.translateY = 10
      instance.translateZ = 15
      result === Affine(xt = -5, xx = 2, yt = 35, yy = 0.5, zz = 0.5, zt = 15)
    }
    
    "react to rotation changes" in {
      todo
    }

    "react to combinations of scale and rotate" in {
      todo
    }
    
    "react to combinations of scale, rotate and position" in {
      todo
    }
    
    "react to extra transformations" in {
      todo
    }

    "react to ..." in {
      todo
    }
  }
}