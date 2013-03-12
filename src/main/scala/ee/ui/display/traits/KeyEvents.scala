package ee.ui.display.traits

import ee.ui.events.KeyEvent
import ee.ui.events.CharacterTypedEvent
import ee.ui.members.ReadOnlyEvent

trait KeyEvents {
  val onKeyUp = ReadOnlyEvent[KeyEvent]
  val onKeyDown = ReadOnlyEvent[KeyEvent]
  val onCharacterTyped = ReadOnlyEvent[CharacterTypedEvent]
}