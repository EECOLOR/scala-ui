
package ee.ui.application

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.display.Scene
import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.display.implementation.EmptyWindowImplementationHandler

class ApplicationTest extends Specification {

  xonly

  // introducing FakeEngine to show a potential bug in the DelayedInit trait 
  trait FakeEngine {
    val windowImplementationHandler = EmptyWindowImplementationHandler
  }

  class TestApplication extends StubApplication with FakeEngine {
    // the body should be kept emtpy to make sure DelayedInit of Application is tested correctly
  }

  "Application" should {
    "start" in {
      var started = false

      val application =
        new TestApplication {
          override def start(window: Window) = {
            started = true
          }
        }
      application.start()

      started === true
    }

    "be able to show a window" in {
      var shown = false

      val application =
        new TestApplication {
          override def start(window: Window) = {
            window.showing.change { shown = _ }
            show(window)
          }
        }
      application.start()

      shown === true
    }

    "be able to hide a window" in {
      var shown = true

      val application =
        new TestApplication {
          override def start(window: Window) = {
            show(window)
            window.showing.change { shown = _ }
            hide(window)
          }
        }
      application.start()

      shown === false
    }

    "should call an window implementation handler when a window is shown or hidden" in {
      var shownWindow = new Window {}
      var hiddenWindow = new Window {}
      var expectedWindow = new Window {}

      val application =
        new Application {
          val windowImplementationHandler = new WindowImplementationHandler {
            def show(window: Window) = shownWindow = window
            def hide(window: Window) = hiddenWindow = window
          }

          def start(window: Window) = {
            expectedWindow = window
            show(window)
            hide(window)
          }
        }
      application.start()
      
      shownWindow === expectedWindow and
        hiddenWindow === expectedWindow
    }

  }
}