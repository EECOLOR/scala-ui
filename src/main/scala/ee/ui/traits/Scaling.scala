package ee.ui.traits

import ee.ui.properties.Property

trait Scaling {
  private val _scaleX = new Property(1d)
  def scaleX = _scaleX
  def scaleX_=(value: Double) = _scaleX.value = value

  private val _scaleY = new Property(1d)
  def scaleY = _scaleY
  def scaleY_=(value: Double) = _scaleY.value = value

  private val _scaleZ = new Property(1d)
  def scaleZ = _scaleZ
  def scaleZ_=(value: Double) = _scaleZ.value = value
}