package ee.ui.application

import ee.ui.display.Window
import ee.ui.display.implementation.WindowImplementationHandler

abstract class Application {
  val windowImplementationHandler: WindowImplementationHandler

  def start():Unit = start(new Window())
  def start(window: Window): Unit

  def show(window: Window) = {
    windowImplementationHandler.show(window)
    Window.show(window)
  }

  def hide(window: Window) = {
    windowImplementationHandler.hide(window)
    Window.hide(window)
  }
}