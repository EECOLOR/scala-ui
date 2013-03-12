package ee.ui.display.shape

import ee.ui.display.Shape
import ee.ui.members.Property

class Rectangle extends Shape {
  private val _arcWidth = new Property(0d)
  def arcWidth = _arcWidth
  def arcWidth_=(value: Double) = _arcWidth.value = value

  private val _arcHeight = new Property(0d)
  def arcHeight = _arcHeight
  def arcHeight_=(value: Double) = _arcHeight.value = value
}