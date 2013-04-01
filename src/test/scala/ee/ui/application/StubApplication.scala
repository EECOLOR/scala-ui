package ee.ui.application

import ee.ui.display.Window
import ee.ui.implementation.WindowImplementationHandler

abstract class StubApplication extends Application {
  def start(window:Window):Unit = {}
}