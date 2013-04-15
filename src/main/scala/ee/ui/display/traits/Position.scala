package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait ReadOnlyPosition {
  protected val _x = Property(0d)
  def x: ReadOnlyProperty[Double] = _x

  protected val _y = Property(0d)
  def y: ReadOnlyProperty[Double] = _y
}

trait RestrictedPosition extends ReadOnlyPosition {
  def x_=(value: Double)(implicit ev: AccessRestriction) = _x.value = value
  def y_=(value: Double)(implicit ev: AccessRestriction) = _y.value = value
}

trait Position extends RestrictedPosition {
  override def x = _x
  def x_=(value: Double):Unit = _x.value = value

  override def y = _y
  def y_=(value: Double) = _y.value = value
}