package ee.ui.primitives.transformation

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.primitives.Transformation
import ee.ui.primitives.TransformationTest
import ee.ui.primitives.Point
import ee.ui.primitives.Bounds

object IdentityTest extends Specification {

  xonly

  "Identity" should {

    "extend Transformation" in {
      SubtypeTest[Identity.type <:< Transformation]
    }

    "has the correct values" in {
      TransformationTest.checkDefaultValues(Identity)
    }

    "overrides isPureTranslation" in {
      // Actually not testable
      Identity.isPureTranslation
    }

    "overrides isIdentity" in {
      // Actually not testable
      Identity.isIdentity
    }

    "overrides transform point" in {
      val p = Point(0, 0)
      Identity.transform(p) eq p
    }

    "overrides transform bounds" in {
      val b = Bounds(0, 0, 0, 0)
      Identity.transform(b) eq b
    }

    "overrides ++" in {
      val t = new Transformation {}
      (Identity ++ t) eq t
    }
  }
}