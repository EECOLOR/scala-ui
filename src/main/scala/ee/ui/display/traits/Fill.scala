package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.display.primitives.Color

trait ReadOnlyFill {
  protected val _fill = Property[Option[Color]](None)
  def fill: ReadOnlyProperty[Option[Color]] = _fill
}

trait Fill extends ReadOnlyFill {
  override def fill: Property[Option[Color]] = _fill
  def fill_=(value: Color) = _fill.value = Some(value)
  def fill_=(value: Option[Color]) = _fill.value = value
}