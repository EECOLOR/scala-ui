package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait ReadOnlyText {
  protected val _text = Property("")
  def text: ReadOnlyProperty[String] = _text
}

trait Text extends ReadOnlyText {
  override def text = _text
  def text_=(value: String) = _text.value = value
}