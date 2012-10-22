package ee.ui.nativeElements

import ee.ui.traits.Position
import ee.ui.traits.Size
import ee.ui.properties.Property

class Rectangle extends Shape with Position with Size {
  private val _arcWidth = new Property(0d)
  def arcWidth = _arcWidth
  def arcWidth_=(value: Double) = _arcWidth.value = value

  private val _arcHeight = new Property(0d)
  def arcHeight = _arcHeight
  def arcHeight_=(value: Double) = _arcHeight.value = value
}