package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait ReadOnlyTranslation {
  protected val _translateX = Property(0d)
  def translateX: ReadOnlyProperty[Double] = _translateX

  protected val _translateY = Property(0d)
  def translateY: ReadOnlyProperty[Double] = _translateY

  protected val _translateZ = Property(0d)
  def translateZ: ReadOnlyProperty[Double] = _translateZ
}

trait Translation extends ReadOnlyTranslation {
  override def translateX = _translateX
  def translateX_=(value: Double): Unit = _translateX.value = value

  override def translateY = _translateY
  def translateY_=(value: Double) = _translateY.value = value

  override def translateZ = _translateZ
  def translateZ_=(value: Double): Unit = _translateZ.value = value
}