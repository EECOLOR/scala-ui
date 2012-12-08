package ee.ui.display.traits

import ee.ui.events.KeyEvent
import ee.ui.events.CharacterTypedEvent
import ee.ui.events.ReadOnlyEvent

trait KeyEvents {
  val onKeyUp = new ReadOnlyEvent[KeyEvent]
  val onKeyDown = new ReadOnlyEvent[KeyEvent]
  val onCharacterTyped = new ReadOnlyEvent[CharacterTypedEvent]
}