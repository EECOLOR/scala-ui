package ee.ui.traits

import ee.ui.events.KeyEvent
import ee.ui.events.CharacterTypedEvent
import ee.ui.events.Event

trait KeyEvents {
  val onKeyUp = new Event[KeyEvent]
  val onKeyDown = new Event[KeyEvent]
  val onCharacterTyped = new Event[CharacterTypedEvent]
}