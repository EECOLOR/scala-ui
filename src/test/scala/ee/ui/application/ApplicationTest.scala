
package ee.ui.application

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.display.Scene
import utils.TypeTest
import ee.ui.members.ObservableSeq
import scala.collection.mutable.ListBuffer
import ee.ui.events.Change
import ee.ui.events.Add
import ee.ui.events.Remove
import ee.ui.implementation.WindowImplementationHandler

class ApplicationTest extends Specification {

  xonly
  isolated

  val testApplication = new TestApplication

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

    "call an window implementation handler when a window is shown or hidden" in {
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

    "have an exit method" in {
      testApplication.exit()
    }
    "have an exit handler" in {
      testApplication.exitHandler
      ok
    }
    
    "have an observable read only list of windows" in {
      TypeTest[ObservableSeq[Window]].forInstance(testApplication.windows)
    }
    "update the window list when showing or hiding windows" in {
      val window1 = new Window
      val window2 = new Window
      val resultingEvents = ListBuffer.empty[Change[Window]]
      
      testApplication.windows.change { resultingEvents += _ }
      testApplication.show(window1)
      testApplication.show(window2)
      testApplication.hide(window1)
      testApplication.hide(window2)
      
      resultingEvents.toSeq === Seq(
          Add(0, window1), 
          Add(1, window2), 
          Remove(0, window1), 
          Remove(0, window2))
    }
    "have a platform" in {
      todo
    }
    "stop when all windows are closed and the appropriate setting is set" in {
      todo
    }
  }
}