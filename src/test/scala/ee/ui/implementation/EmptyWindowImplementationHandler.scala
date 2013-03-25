package ee.ui.implementation

import ee.ui.display.Window

object EmptyWindowImplementationHandler extends WindowImplementationHandler {
  def show(window:Window) = {}
  def hide(window:Window) = {}
}