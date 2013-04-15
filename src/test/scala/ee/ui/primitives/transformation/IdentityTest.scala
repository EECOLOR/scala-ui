package ee.ui.primitives.transformation

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.primitives.Transformation
import ee.ui.primitives.TransformationTest

object IdentityTest extends Specification {
  
  "Identity" should {
    
    "extend Transformation" in {
      SubtypeTest[Identity.type <:< Transformation]
    }
    
    "has the correct values" in {
      TransformationTest.checkDefaultValues(Identity)
    }
    
  }
  
}