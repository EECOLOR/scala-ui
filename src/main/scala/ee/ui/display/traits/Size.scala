package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty

trait ReadOnlySize {
  protected val _width = Property(0d)
  def width:ReadOnlyProperty[Double] = _width
  
  protected val _height = Property(0d)
  def height:ReadOnlyProperty[Double] = _height
}

trait Size extends ReadOnlySize {
  override def width = _width 
  def width_=(value: Double) = _width.value = value

  override def height = _height 
  def height_=(value: Double) = _height.value = value
}