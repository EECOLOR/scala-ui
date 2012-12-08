package ee.ui.display.traits

import ee.ui.properties.Property
import ee.ui.primitives.Rotate
import ee.ui.primitives.Point

trait Rotation {
  /**
   * In degrees
   */
  private val _rotation = new Property(0d)
  def rotation = _rotation
  def rotation_=(value: Double) = _rotation.value = value

  private val _rotationAxis = new Property(Rotate.Z_AXIS)
  def rotationAxis = _rotationAxis
  def rotationAxis_=(value: Point) = _rotationAxis.value = value
  
}

