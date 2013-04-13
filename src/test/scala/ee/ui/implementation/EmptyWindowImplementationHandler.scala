package ee.ui.implementation

import ee.ui.implementation.contracts.WindowContract

object EmptyWindowImplementationHandler extends WindowImplementationHandler {
  def show(windowContract:WindowContract) = {}
  def hide(windowContract:WindowContract) = {}
}