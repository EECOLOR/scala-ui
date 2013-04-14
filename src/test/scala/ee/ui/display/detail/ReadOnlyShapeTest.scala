package ee.ui.display.detail

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.display.traits.ReadOnlyFill

object ReadOnlyShapeTest extends Specification {
  
  xonly
  
  "ReadOnlyShape" should {
    
    "extend the correct types" in {
      SubtypeTest[ReadOnlyShape <:< ReadOnlyNode with ReadOnlyFill]
    }
  }
}