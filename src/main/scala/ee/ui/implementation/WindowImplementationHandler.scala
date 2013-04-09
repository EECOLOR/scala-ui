package ee.ui.implementation

import ee.ui.display.Window
import ee.ui.implementation.contracts.WindowContract

trait WindowImplementationHandler {
  def show(windowContract:WindowContract):Unit
  def hide(windowContract:WindowContract):Unit
}