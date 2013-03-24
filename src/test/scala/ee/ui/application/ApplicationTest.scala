
package ee.ui.application

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.display.Scene

class ApplicationTest extends Specification {

  xonly
  
  "Application" should {
    "start" in {
      var started = false

      new Application {
        def start(window:Window) = {
          started = true
        }
      }
      
      started === true
    }
    
    "be able to show a window" in {
      new Application {
        def start(window:Window) = {
          show(window)
        }
      }
      ok
    }
  }
}