
package ee.ui.application

import scala.collection.mutable.ListBuffer
import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.events.Add
import ee.ui.events.Change
import ee.ui.events.Remove
import ee.ui.implementation.ExitHandler
import ee.ui.implementation.WindowImplementationHandler
import ee.ui.implementation.contracts.WindowContract
import ee.ui.members.ObservableSeq
import ee.ui.system.RestrictedAccess
import utils.SignatureTest
import ee.ui.implementation.TextHelper

class ApplicationTest extends Specification {

  xonly
  isolated

  val testApplication = new TestApplication

  def getWindowFromContract(windowContract:WindowContract) = 
    WindowContract.internalWindow(windowContract)(RestrictedAccess)
  
  "Application" should {
    
    "call start with a window" in {
      var started = false

      val application =
        new TestApplication {
          override def start(window: Window) = {
            started = true
          }
        }
      application.start()

      started
    }

    "set the showing property of a window to true" in {
      var shown = false

      val application =
        new TestApplication {
          override def start(window: Window) = {
            window.showing.change { shown = _ }
            show(window)
          }
        }
      application.start()

      shown
    }

    "set the showing property of a window to false" in {
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

      !shown
    }

    "call a window implementation handler when a window is shown or hidden" in {
      var shownWindow = new Window {}
      var hiddenWindow = new Window {}
      var expectedWindow = new Window {}

      val application =
        new TestApplication {
          override val windowImplementationHandler = new WindowImplementationHandler {
            def show(windowContract: WindowContract) = 
              shownWindow = getWindowFromContract(windowContract)
            def hide(windowContract: WindowContract) = 
              hiddenWindow = getWindowFromContract(windowContract)
          }

          override def start(window: Window) = {
            expectedWindow = window
            show(window)
            hide(window)
          }
        }
      application.start()

      shownWindow === expectedWindow and
        hiddenWindow === expectedWindow
    }

    "call the exit handler when the exit method is called" in {
      var exitHandlerCalled = false
      val application =
        new TestApplication {
          override val exitHandler = new ExitHandler {
            def exit(application: Application) = exitHandlerCalled = true
          }
        }
      application.exit()

      exitHandlerCalled
    }

    "call exit when all windows are closed" in {
      var exitCalled = false
      val application =
        new TestApplication {

          override def exit() = exitCalled = true

          override def start(window: Window) = {
            show(window)
            hide(window)
          }
        }
      application.start()

      exitCalled
    }
    
    "have an ApplicationSettings instance" in {
      SignatureTest[Application, ApplicationSettings](_.settings)
    }
    
    "not call exit if explicitExit in the settings is false" in {
      var exitCalled = false
      val application =
        new TestApplication {

          override def exit() = exitCalled = true

          override def start(window: Window) = {
            show(window)
            hide(window)
          }
        }
      application.settings.implicitExit = false
      application.start()

      !exitCalled
    }

    "have an observable read only list of windows" in {
      SignatureTest[Application, ObservableSeq[Window]](_.windows)
    }
    
    "update the window list when showing or hiding windows" in {
      val window1 = new Window
      val window2 = new Window
      
      val resultingEvents = ListBuffer.empty[Change[Window]]
      testApplication.windows change { resultingEvents += _ }
      
      testApplication show window1
      testApplication show window2
      testApplication hide window1
      testApplication hide window2

      resultingEvents.toSeq === Seq(
        Add(0, window1),
        Add(1, window2),
        Remove(0, window1),
        Remove(0, window2))
    }
    
    "have a stop method" in {
      SignatureTest[Application, Unit](_.stop())
    }
    
    "have a text helper" in {
      SignatureTest[Application, TextHelper](_.textHelper)
    }
  }
  
}