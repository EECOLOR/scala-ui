package ee.ui.display.text

import ee.ui.primitives.KeyCode._
import ee.ui.system.OperatingSystem
import ee.ui.display.traits.KeyEvents

trait SingleLineTextKeyHandling extends SingleLineTextKeyHandlers with SingleLikeTextKeyBindings { self: KeyEvents with TextInputLike =>
}

trait SingleLineTextKeyHandlers extends TextKeyHandlers { self: TextInputLike =>
	// defines no special methods, only extra bindings
}

trait SingleLikeTextKeyBindings extends TextKeyBindings { self: KeyEvents with TextKeyHandlers =>
  override val bindings = textKeyBindings ++ Map(
    HOME -> home, UP -> home, KP_UP -> home,
    END -> end, DOWN -> end, KP_DOWN -> end,
    SHIFT + UP -> selectHome, SHIFT + KP_UP -> selectHome,
    SHIFT + DOWN -> selectEnd, SHIFT + KP_DOWN -> selectEnd)

  private val operatingSystemSpecificBindings =
    OperatingSystem.current match {
      case OperatingSystem.WINDOWS => windowsBindings
      case OperatingSystem.MAC => macBindings
      case OperatingSystem.LINUX => linuxBindings
      case OperatingSystem.SOLARIS => solarisBindings
      case OperatingSystem.UNKNOWN => unknownBindings
    }

  private val windowsBindings = nonMacBindings
  private val solarisBindings = nonMacBindings
  private val unknownBindings = nonMacBindings
  private val linuxBindings = nonMacBindings

  private val macBindings = Map(
    SHIFT + HOME -> selectHomeExtend,
    SHIFT + END -> selectEndExtend,
    SHIFT + META + LEFT -> selectHomeExtend, SHIFT + META + KP_LEFT -> selectHomeExtend,
    SHIFT + META + RIGHT -> selectEndExtend, SHIFT + META + KP_RIGHT -> selectEndExtend,
    META + LEFT -> home, META + KP_LEFT -> home,
    META + RIGHT -> end, META + KP_RIGHT -> end)

  private val nonMacBindings = Map(
    SHIFT + HOME -> selectHome,
    SHIFT + END -> selectEnd)
}