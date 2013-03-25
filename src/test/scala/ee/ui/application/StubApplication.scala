package ee.ui.application

import ee.ui.display.Window
import ee.ui.implementation.WindowImplementationHandler

abstract class StubApplication extends Application {
  val windowImplementationHandler:WindowImplementationHandler
  var isStarted = false
  def start(window:Window) = isStarted = true
}