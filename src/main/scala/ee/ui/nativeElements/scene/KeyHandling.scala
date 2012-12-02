package ee.ui.nativeElements.scene

import ee.ui.traits.KeyBindings
import ee.ui.nativeElements.Scene
import ee.ui.traits.KeyEvents
import ee.ui.primitives.KeyCode._

trait KeyHandling extends KeyBindings { self: Scene with KeyEvents =>
  protected val tabFocusBindings = Map(
    TAB -> focusNext,
    SHIFT + TAB -> focusPrevious)

  val bindings = tabFocusBindings
  
  //TODO implement
  def focusNext() = {}
  def focusPrevious() = {}
  
  onCharacterTyped { e => println(e)}
  onKeyDown { e => println(e)}
}