package ee.ui.display.traits

import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess

trait ReadOnlySize {
  protected val _width = ReadOnlyProperty(0d)
  def width = _width
  
  protected val _height = ReadOnlyProperty(0d)
  def height = _height
}

trait Size extends ReadOnlySize {
  def width_=(value: Double) = ReadOnlyProperty.setValue(_width, value)(RestrictedAccess)

  def height_=(value: Double) = ReadOnlyProperty.setValue(_height, value)(RestrictedAccess)
}