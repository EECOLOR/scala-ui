package ee.ui.layout

import ee.ui.Group
import ee.ui.Stylable
import ee.ui.traits.LayoutSize
import ee.ui.Node
import ee.ui.properties.Add
import ee.ui.properties.Remove
import ee.ui.properties.Clear
import scala.collection.mutable.ListBuffer
import ee.ui.traits.RestrictedAccess
import ee.ui.nativeElements.Text
import ee.ui.primitives.Point

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

