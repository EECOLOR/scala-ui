package ee.ui.events

import ee.ui.primitives.KeyCode

case class KeyEvent(
  code: KeyCode,
  text: String,
  shiftDown: Boolean,
  ctrlDown: Boolean,
  altDown: Boolean,
  metaDown: Boolean) {

}