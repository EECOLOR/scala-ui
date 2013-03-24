package ee.ui.display.implementation

import ee.ui.display.Window

trait WindowImplementationHandler {
  def show(window:Window):Unit
  def hide(window:Window):Unit
}