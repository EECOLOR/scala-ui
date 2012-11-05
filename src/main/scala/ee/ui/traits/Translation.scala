package ee.ui.traits

import ee.ui.properties.Property

trait Translation {
  private val _translateX = new Property(0d)
  def translateX = _translateX
  def translateX_=(value: Double) = _translateX.value = value

  private val _translateY = new Property(0d)
  def translateY = _translateY
  def translateY_=(value: Double) = _translateY.value = value

  private val _translateZ = new Property(0d)
  def translateZ = _translateZ
  def translateZ_=(value: Double) = _translateZ.value = value
}