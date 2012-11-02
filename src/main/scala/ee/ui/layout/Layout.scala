package ee.ui.layout

import ee.ui.Group
import ee.ui.Stylable
import ee.ui.traits.LayoutSize
import ee.ui.Node
import ee.ui.properties.Add
import ee.ui.properties.Remove
import ee.ui.properties.Clear
import scala.collection.mutable.ListBuffer

trait Layout extends Stylable { self: Group =>
  
  /**
   * At this point the size of the group and the size of the non parent related 
   * nodes is known. These methods are used to calculate the size of the remaining 
   * nodes.
   */
  def calculateChildWidth(node: Node with ParentRelatedWidth): Width
  def calculateChildHeight(node: Node with ParentRelatedHeight): Height

  def determineTotalChildWidth(getChildWidth: Node => Width): Width
  def determineTotalChildHeight(getChildHeight: Node => Height): Height

  def updateLayout: Unit

}

object Layout {
  def determineTotalChildWidth(group:Group, getChildWidth:Node => Width):Width =
    (group.children foldLeft 0d) { (total, node) => math max (total, getChildWidth(node)) }
  
  def determineTotalChildHeight(group:Group, getChildHeight:Node => Height):Height = 
    (group.children foldLeft 0d) { (total, node) => math max (total, getChildHeight(node)) }
}
