package ee.ui.layout

import ee.ui.Group
import ee.ui.traits.RestrictedAccess

trait CanResizeToChildren { self:Group =>
  def resizeToChildren: Unit = {
    implicit val access = RestrictedAccess

    width = widthOfChildren
    height = heightOfChildren
  }

  def widthOfChildren: Double = maxChildWidth
  def heightOfChildren: Double = maxChildHeight

  def maxChildWidth: Double =
    children.map(_.width.value).max

  def maxChildHeight: Double =
    children.map(_.height.value).max
}