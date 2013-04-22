package ee.ui.display.detail

import org.specs2.mutable.Specification
import ee.ui.display.traits.CalculatedBounds
import utils.SubtypeTest
import ee.ui.display.traits.{ReadOnlyText => ReadOnlyTextTrait}


object ReadOnlyTextTest extends Specification {

  xonly
  
  "ReadOnlyText" should {
    
    "have the correct type" in {
      SubtypeTest[ReadOnlyText <:< ReadOnlyTextTrait]
    }
  }
}