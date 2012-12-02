package ee.ui.events

case class CharacterTypedEvent(
  character: String,
  shiftDown: Boolean,
  controlDown: Boolean,
  altDown: Boolean,
  metaDown: Boolean) {

}