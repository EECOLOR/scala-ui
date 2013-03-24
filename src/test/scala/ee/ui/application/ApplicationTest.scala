
package ee.ui.application

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.display.Scene
import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.display.implementation.EmptyWindowImplementationHandler

class ApplicationTest extends Specification {

  xonly

  "Application" should {
    "start" in {
      var started = false

      new StubApplication {
        override def start(window: Window) = {
          started = true
        }
      }

      started === true
    }

    "be able to show a window" in {
      var shown = false

      new StubApplication {
        override def start(window: Window) = {
          window.showing.change { shown = _ }
          show(window)
        }
      }

      shown === true
    }

    "be able to hide a window" in {
      var shown = true

      new StubApplication {
        override def start(window: Window) = {
          show(window)
          window.showing.change { shown = _ }
          hide(window)
        }
      }

      shown === false
    }

    "should call an window implementation handler when a window is shown or hidden" in {
      var shownWindow = new Window {}
      var hiddenWindow = new Window {}
      var expectedWindow = new Window {}

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

      shownWindow === expectedWindow and
      hiddenWindow === expectedWindow
    }

  }
}