package ee.ui.application

import ee.ui.display.Window
import ee.ui.implementation.WindowImplementationHandler
import ee.ui.members.ObservableArrayBuffer
import ee.ui.members.ObservableSeq
import ee.ui.system.RestrictedAccess
import ee.ui.implementation.ExitHandler

abstract class Application {
  val windowImplementationHandler: WindowImplementationHandler
  val exitHandler:ExitHandler
  val settings:ApplicationSettings

  val windows = ObservableSeq.empty[Window]
  
  def start():Unit = start(new Window())
  def start(window: Window): Unit

  def exit():Unit = exitHandler.exit(this)
  
  def stop():Unit = {}
  
  def show(window: Window) = {
    windowImplementationHandler.show(window)
    Window.show(window)
    ObservableSeq.add(windows, window)(RestrictedAccess) 
  }

  def hide(window: Window) = {
    ObservableSeq.remove(windows, window)(RestrictedAccess) 
    Window.hide(window)
    windowImplementationHandler.hide(window)
    
    if (windows.isEmpty && settings.implicitExit) exit()
  }
}