package ee.ui.display.traits

import ee.ui.display.primitives.Color
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty

trait ReadOnlyStroke {
  protected val _stroke = Property[Option[Color]](None)
  def stroke: ReadOnlyProperty[Option[Color]] = _stroke
}

trait Stroke extends ReadOnlyStroke {
  override def stroke: Property[Option[Color]] = _stroke
  def stroke_=(value: Color) = _stroke.value = Some(value)
  def stroke_=(value: Option[Color]) = _stroke.value = value
}