package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait ReadOnlySize {
  protected val _width = Property(0d)
  def width: ReadOnlyProperty[Double] = _width

  protected val _height = Property(0d)
  def height: ReadOnlyProperty[Double] = _height
}

trait RestrictedSize extends ReadOnlySize {
  def width_=(value: Double)(implicit ev: AccessRestriction) = _width.value = value
  def height_=(value: Double)(implicit ev: AccessRestriction) = _height.value = value
}

trait Size extends RestrictedSize {
  override def width = _width
  def width_=(value: Double):Unit = _width.value = value

  override def height = _height
  def height_=(value: Double) = _height.value = value
}