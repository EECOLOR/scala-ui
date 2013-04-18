package ee.ui.display.detail

import org.specs2.mutable.Specification
import ee.ui.display.traits.CalculatedBounds
import utils.SubtypeTest

object ReadOnlyNodeTest extends Specification {
  
  xonly
  
  "ReadOnlyNode" should {
    "have the correct type" in {
      SubtypeTest[ReadOnlyNode <:< CalculatedBounds]
    }
  }
}