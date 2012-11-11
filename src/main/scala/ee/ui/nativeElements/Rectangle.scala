package ee.ui.nativeElements

import ee.ui.traits.Size
import ee.ui.properties.Property
import ee.ui.traits.LayoutPosition
import ee.ui.traits.LayoutSize
import ee.ui.traits.ExplicitSize

class Rectangle extends Shape {
  private val _arcWidth = new Property(0d)
  def arcWidth = _arcWidth
  def arcWidth_=(value: Double) = _arcWidth.value = value

  private val _arcHeight = new Property(0d)
  def arcHeight = _arcHeight
  def arcHeight_=(value: Double) = _arcHeight.value = value
}