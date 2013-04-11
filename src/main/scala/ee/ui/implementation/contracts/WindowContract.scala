package ee.ui.implementation.contracts

import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.Window
import ee.ui.display.traits.ReadOnlyTitle
import ee.ui.display.traits.Size
import ee.ui.system.AccessRestriction
import ee.ui.display.detail.ReadOnlyScene
import ee.ui.display.detail.ReadOnlyScene

case class WindowContract(protected val internalWindow:Window) {
  val window:Size with ReadOnlyTitle with ReadOnlyScene = internalWindow
}

object WindowContract {
  def internalWindow(windowContract:WindowContract)(implicit ev:AccessRestriction) = windowContract.internalWindow
}