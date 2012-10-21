package ee.ui.traits

import ee.ui.properties.Property
import ee.ui.primitives.Paint

trait Fill {
  def defaultFill:Paint
  
  private val _fill = new Property[Option[Paint]](Some(defaultFill))
  def fill = _fill
  def fill_=(value:Paint) = _fill.value = Some(value)
  def fill_=(value:Option[Paint]) = _fill.value = value
}