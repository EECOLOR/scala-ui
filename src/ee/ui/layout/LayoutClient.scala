package ee.ui.layout

import ee.ui.properties.Property

trait LayoutClient {
  private val _includeInLayout = new Property[Boolean](true)
  def includeInLayout = _includeInLayout
  def includeInLayout_=(value: Boolean) = _includeInLayout.value = value
}