package ee.ui.display.shapes.detail

import org.specs2.mutable.Specification
import utils.SignatureTest

object TextAlignmentTest extends Specification {

  xonly
  
  "TextAlignment" should {
    
    "have the correct properties" in {
      SignatureTest[TextAlignment.type, TextAlignment](_.LEFT)
      SignatureTest[TextAlignment.type, TextAlignment](_.CENTER)
      SignatureTest[TextAlignment.type, TextAlignment](_.RIGHT)
      SignatureTest[TextAlignment.type, TextAlignment](_.JUSTIFY)
    }
    
  }
}