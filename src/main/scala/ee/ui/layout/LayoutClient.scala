package ee.ui.layout

import ee.ui.properties.Property
import ee.ui.Group
import ee.ui.Node
import ee.ui.traits.RestrictedAccess

trait LayoutClient { self: Node =>
  private val _includeInLayout = new Property[Boolean](true)
  def includeInLayout = _includeInLayout
  def includeInLayout_=(value: Boolean) = _includeInLayout.value = value

}