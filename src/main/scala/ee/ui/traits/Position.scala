package ee.ui.traits

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty

trait Position {
  private val _x = new Property(0d)
  def x = _x
  def x_=(value: Double) = x.value = value

  private val _y = new Property(0d)
  def y = _y
  def y_=(value: Double) = y.value = value
}

trait LayoutPosition extends Position {
  def x_=(value: Double)(implicit ev: AccessRestriction) = super.x = value
  def y_=(value: Double)(implicit ev: AccessRestriction) = super.y = value
}

trait ExplicitPosition extends LayoutPosition {
  override def x_=(value: Double) = super.x_=(value)(RestrictedAccess)
  override def y_=(value: Double) = super.y_=(value)(RestrictedAccess)
}
