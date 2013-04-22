package ee.ui.display

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.display.detail.ReadOnlyText
import ee.ui.display.traits.{Text => TextTrait}
import ee.ui.display.implementation.contracts.TextContract

object TextTest extends Specification {
  
  xonly
  
  "Text" should {
    
    "have the correct types" in {
      SubtypeTest[Text <:< Node with TextContract with TextTrait]
    }
  }
  
}