package ee.ui.traits

import ee.ui.primitives.KeyCode
import ee.ui.primitives.KeyCode._

trait KeyBindings { self: KeyEvents =>
  val bindings: Map[KeyCombination, () => Unit]

  object CTRL extends CombinationBuilder(ctrl = true)
  object ALT extends CombinationBuilder(alt = true)
  object SHIFT extends CombinationBuilder(shift = true)
  object META extends CombinationBuilder(meta = true)

  onKeyDown { e =>
    val keyCombination = KeyCombination(e.code, e.shiftDown, e.ctrlDown, e.altDown, e.metaDown)
    (bindings get keyCombination) foreach (_())
  }

  implicit def keyCodeToKeyCombination(keyCode: KeyCode): KeyCombination = KeyCombination(keyCode)
}

case class CombinationBuilder(
  shift: Boolean = false,
  ctrl: Boolean = false,
  alt: Boolean = false,
  meta: Boolean = false) {

  def +(keyCode: KeyCode) = KeyCombination(keyCode, shift, ctrl, alt, meta)

  def +(other: CombinationBuilder) =
    CombinationBuilder(
      shift || other.shift,
      ctrl || other.ctrl,
      alt || other.alt,
      meta || other.meta)
}

case class KeyCombination(
  keyCode: KeyCode,
  shift: Boolean = false,
  ctrl: Boolean = false,
  alt: Boolean = false,
  meta: Boolean = false) {

  def ->(handler: () => Unit): (KeyCombination, () => Unit) = (this, handler)
}

