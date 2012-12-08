package ee.ui.display.shape
import ee.ui.properties.Property
import ee.ui.display.Shape

class Rectangle extends Shape {
  private val _arcWidth = new Property(0d)
  def arcWidth = _arcWidth
  def arcWidth_=(value: Double) = _arcWidth.value = value

  private val _arcHeight = new Property(0d)
  def arcHeight = _arcHeight
  def arcHeight_=(value: Double) = _arcHeight.value = value
}