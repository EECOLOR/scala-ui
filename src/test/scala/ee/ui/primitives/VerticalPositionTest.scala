package ee.ui.primitives

import org.specs2.mutable.Specification
import utils.SignatureTest

object VerticalPositionTest extends Specification {

  xonly
  
  "VerticalPosition" should {
    
    "have the correct properties" in {
      SignatureTest[VerticalPosition.type, VerticalPosition](_.TOP)
      SignatureTest[VerticalPosition.type, VerticalPosition](_.CENTER)
      SignatureTest[VerticalPosition.type, VerticalPosition](_.BOTTOM)
    }
    
  }
}