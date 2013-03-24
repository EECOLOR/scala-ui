package ee.ui.display.implementation

import org.specs2.mutable.Specification
import ee.ui.display.Window

class WindowImplementationHandlerTest extends Specification {
  xonly
  
  "WindowImplementationHandler" should {
    "have a show and hide method" in {
      val w:WindowImplementationHandler = new WindowImplementationHandler {
        def show(window:Window) = {}
        def hide(window:Window) = {}
      }
      w.show(new Window())
      w.hide(new Window())
    }
  }
}