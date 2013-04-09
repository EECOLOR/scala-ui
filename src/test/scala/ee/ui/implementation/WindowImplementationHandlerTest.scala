package ee.ui.implementation

import org.specs2.mutable.Specification
import ee.ui.display.Window
import utils.SignatureTest
import ee.ui.implementation.contracts.WindowContract

class WindowImplementationHandlerTest extends Specification {
  xonly
  
  "WindowImplementationHandler" should {
    "have a show and hide method" in {
      val windowContract = WindowContract(new Window)
      SignatureTest[WindowImplementationHandler, Unit](_.show(windowContract = windowContract))
      SignatureTest[WindowImplementationHandler, Unit](_.hide(windowContract = windowContract))
    }
  }
}