package ee.ui.traits

import ee.ui.properties.Property

trait Translation {
  private val _translationX = new Property(0d)
  def translationX = _translationX
  def translationX_=(value: Double) = _translationX.value = value

  private val _translationY = new Property(0d)
  def translationY = _translationY
  def translationY_=(value: Double) = _translationY.value = value
}