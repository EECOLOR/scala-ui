package ee.ui.layout

import ee.ui.display.Group
import ee.ui.system.RestrictedAccess
import ee.ui.display.Node
import ee.ui.members.Property

trait LayoutClient { self: Node =>
  private val _includeInLayout = new Property[Boolean](true)
  def includeInLayout = _includeInLayout
  def includeInLayout_=(value: Boolean) = _includeInLayout.value = value

}