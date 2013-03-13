package ee.ui.display.implementation

import ee.ui.display.traits.FocusProxy
import ee.ui.display.traits.SizeProxy
import ee.ui.display.traits.PositionProxy
import ee.ui.display.Window
import ee.ui.display.traits.ReadOnlyPosition

case class WindowContract(window: Window) {
  val read = window

  object write extends PositionProxy with SizeProxy with FocusProxy {
    protected val target = window
  }
}