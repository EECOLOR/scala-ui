package ee.ui.application

import ee.ui.display.Window
import ee.ui.implementation.ExitHandler
import ee.ui.implementation.WindowImplementationHandler
import ee.ui.implementation.contracts.WindowContract
import ee.ui.members.ObservableSeq
import ee.ui.members.ReadOnlyProperty.propertyToValue
import ee.ui.system.RestrictedAccess
import ee.ui.implementation.TextHelper

abstract class Application {
  val windowImplementationHandler: WindowImplementationHandler
  val exitHandler:ExitHandler
  val settings:ApplicationSettings
  val textHelper:TextHelper
  
  val windows = ObservableSeq.empty[Window]
  
  def start():Unit = start(new Window())
  def start(window: Window): Unit

  def exit():Unit = exitHandler.exit(this)
  
  def stop():Unit = {}
  
  implicit private val restrictedAccess = RestrictedAccess
  
  def show(window: Window) = {
    windowImplementationHandler.show(WindowContract(window))
    Window.show(window)
    ObservableSeq.add(windows, window) 
  }

  def hide(window: Window) = {
    ObservableSeq.remove(windows, window) 
    Window.hide(window)
    windowImplementationHandler.hide(WindowContract(window))
    
    if (windows.isEmpty && settings.implicitExit) exit()
  }
}