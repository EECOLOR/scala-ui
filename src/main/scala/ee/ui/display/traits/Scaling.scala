package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait ReadOnlyScaling {
  protected val _scaleX = Property(1d)
  def scaleX: ReadOnlyProperty[Double] = _scaleX

  protected val _scaleY = Property(1d)
  def scaleY: ReadOnlyProperty[Double] = _scaleY

  protected val _scaleZ = Property(1d)
  def scaleZ: ReadOnlyProperty[Double] = _scaleZ
}

trait Scaling extends ReadOnlyScaling {
  override def scaleX = _scaleX
  def scaleX_=(value: Double): Unit = _scaleX.value = value

  override def scaleY = _scaleY
  def scaleY_=(value: Double) = _scaleY.value = value

  override def scaleZ = _scaleZ
  def scaleZ_=(value: Double): Unit = _scaleZ.value = value
}