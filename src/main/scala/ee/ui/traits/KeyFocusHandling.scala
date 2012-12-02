package ee.ui.traits

import ee.ui.primitives.KeyCode._
import ee.ui.primitives.KeyCode

trait KeyFocusHandling extends TabFocusHandling with ArrowFocusHandling { self: KeyTraits =>

  protected val keyFocusBindings = tabFocusBindings ++ arrowFocusBindings
  
  override val bindings = keyFocusBindings

}

trait TabFocusHandling extends KeyBindings { self: KeyTraits =>
  protected val tabFocusBindings = Map(
    TAB -> focusNext,
    SHIFT + TAB -> focusPrevious)

  val bindings = tabFocusBindings
  
  //TODO implement
  def focusNext() = {}
  def focusPrevious() = {}
}

trait ArrowFocusHandling extends KeyBindings { self: KeyTraits =>
 protected val arrowFocusBindings = Map(
    SHIFT + UP -> focusMoveUp,
    SHIFT + DOWN -> focusMoveDown,
    SHIFT + LEFT -> focusMoveLeft,
    SHIFT + RIGHT -> focusMoveRight)

  val bindings = arrowFocusBindings

  //TODO IMPLEMENT
  def focusMoveUp() = {}
  def focusMoveDown() = {}
  def focusMoveLeft() = {}
  def focusMoveRight() = {}
}