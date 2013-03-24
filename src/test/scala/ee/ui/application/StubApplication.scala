package ee.ui.application

import ee.ui.display.Window
import ee.ui.display.implementation.EmptyWindowImplementationHandler

class StubApplication extends Application {
  val windowImplementationHandler = EmptyWindowImplementationHandler
  def start(window:Window) = {}
}