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
   * This method is called by the resize engine to notify the layout that 
   * all it's 'non parent related size' children haven been resized.
   * 
   * This method can be used to do some pre-calculations that can be used in 
   * the calculateChildWidth and calculateChildHeight methods
   */
  def childrenResized():Unit
  
  /**
   * At this point the size of the group and the size of the non parent related 
   * nodes is known. These methods are used to calculate the size of the remaining 
   * nodes.
   */
  def calculateChildWidth(node: Node with ParentRelatedWidth): Width
  def calculateChildHeight(node: Node with ParentRelatedHeight): Height

  def determineTotalChildWidth(totalWidth: Double, nodeWidth: Double): Width
  def determineTotalChildHeight(totalHeight: Double, nodeHeight: Double): Height

  def updateLayout: Unit

  val nodesWithParentRelatedSize = ListBuffer[Node with PartialParentRelatedSize]()
  val nodes = ListBuffer[Node]()
  
  children onChangedIn {
    case Add(_, element) => element match {
      case element:PartialParentRelatedSize => nodesWithParentRelatedSize += element
      case element => nodes += element
    }
    case Remove(_, element) => element match {
	    case element:PartialParentRelatedSize => nodesWithParentRelatedSize += element
	    case element => nodes += element
    }
    case Clear(elements) => {
      nodesWithParentRelatedSize.clear
      nodes.clear
    }
  }
}

object Layout {
  val defaultSizeAccumulator: (Double, Double) => Double = math.max
}