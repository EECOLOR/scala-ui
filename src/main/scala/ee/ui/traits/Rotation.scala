package ee.ui.traits

import ee.ui.properties.Property
import ee.ui.primitives.Point3D
import ee.ui.primitives.Rotate

trait Rotation {
  /**
   * In degrees
   */
  private val _rotation = new Property(0d)
  def rotation = _rotation
  def rotation_=(value: Double) = _rotation.value = value

  private val _axis = new Property(Rotate.Z_AXIS)
  def axis = _axis
  def axis_=(value: Point3D) = _axis.value = value
}

