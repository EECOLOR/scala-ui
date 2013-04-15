package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess
import ee.ui.primitives.transformation.Rotate
import ee.ui.primitives.Point

trait ReadOnlyRotation {
  /**
   * In degrees
   */
  protected val _rotation = Property(0d)
  def rotation: ReadOnlyProperty[Double] = _rotation

  protected val _rotationAxis = Property(Rotate.Z_AXIS)
  def rotationAxis: ReadOnlyProperty[Point] = _rotationAxis
}

trait Rotation extends ReadOnlyRotation {
  override def rotation = _rotation
  def rotation_=(value: Double): Unit = _rotation.value = value

  override def rotationAxis = _rotationAxis
  def rotationAxis_=(value: Point) = _rotationAxis.value = value
}