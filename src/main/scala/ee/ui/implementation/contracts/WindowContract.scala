package ee.ui.implementation.contracts

import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.Window
import ee.ui.display.traits.ReadOnlyTitle
import ee.ui.display.traits.Size
import ee.ui.system.AccessRestriction

case class WindowContract(protected val internalWindow:Window) {
  val window:Size with ReadOnlyTitle = internalWindow
}

object WindowContract {
  def internalWindow(windowContract:WindowContract)(implicit ev:AccessRestriction) = windowContract.internalWindow
}