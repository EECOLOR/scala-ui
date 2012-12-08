package ee.ui.display.implementation

import ee.ui.display.traits.FocusProxy
import ee.ui.display.traits.SizeProxy
import ee.ui.display.traits.PositionProxy
import ee.ui.display.Window

case class WindowContract(window: Window) {
  val read = window

  object write extends PositionProxy with SizeProxy with FocusProxy {
    val target = window
  }
}