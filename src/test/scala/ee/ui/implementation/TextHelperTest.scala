package ee.ui.implementation

import org.specs2.mutable.Specification
import utils.SignatureTest
import ee.ui.display.shapes.Text
import ee.ui.primitives.Point

object TextHelperTest extends Specification {
  
  xonly
  
  "TextHelper" should {
    
    "have the correct methods" in {
      SignatureTest[TextHelper, Text, Int, Point](_ getCaretPosition (_, _))
    }
    
  }
}