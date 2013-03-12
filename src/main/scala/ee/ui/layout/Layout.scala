package ee.ui.layout

import ee.ui.display.Group
import ee.ui.display.Node
import ee.ui.style.Stylable
import ee.ui.system.RestrictedAccess

trait Layout extends ChildWidthCalculator with ChildHeightCalculator with Stylable { self: Group =>

  def updateLayout: Unit

  def updateX(node: Node, x: X) = {
    // we need access to set positions
    implicit val access = RestrictedAccess

    node.x = x - node.shapeBounds.x
  }

  def updateY(node: Node, y: Y) = {
    // we need access to set positions
    implicit val access = RestrictedAccess

    node.y = y - node.shapeBounds.y
  }
}

