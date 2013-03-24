package ee.ui.display.implementation

import org.specs2.mutable.Specification
import ee.ui.display.Window

class WindowImplementationHandlerTest extends Specification {
  xonly
  
  "WindowImplementationHandler" should {
    "have a show method" in {
      val w = new WindowImplementationHandler {
        def show(window:Window) = {}
      }
      w.show(new Window())
    }
  }
}