package ee.ui.implementation

import org.specs2.mutable.Specification

import ee.ui.implementation.contracts.WindowContract
import utils.SignatureTest

object WindowImplementationHandlerTest extends Specification {
  
  xonly
  
  "WindowImplementationHandler" should {
    
    "have a show and hide method" in {
      SignatureTest[WindowImplementationHandler, WindowContract, Unit](_.show(_))
      SignatureTest[WindowImplementationHandler, WindowContract, Unit](_.hide(_))
    }
  }
}