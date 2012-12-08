package ee.ui.layout

import ee.ui.display.Group
import ee.ui.style.Stylable
import ee.ui.properties.Add
import ee.ui.properties.Remove
import ee.ui.properties.Clear
import scala.collection.mutable.ListBuffer
import ee.ui.system.RestrictedAccess
import ee.ui.display.Text
import ee.ui.primitives.Point
import ee.ui.display.Node

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

