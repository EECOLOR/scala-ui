package ee.ui.traits

import ee.ui.properties.Property

trait Focus {
  private val _focused = new Property(false)
  def focused = _focused
  def focused_=(value: Boolean) = focused.value = value
}