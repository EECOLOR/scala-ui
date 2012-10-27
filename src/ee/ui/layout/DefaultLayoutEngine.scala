package ee.ui.layout

import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.Node
import ee.ui.traits.LayoutSize
import ee.ui.traits.RestrictedAccess

class DefaultLayoutEngine extends LayoutEngine {
  def layout(stage: Stage): Unit = {
    stage.scene foreach layout
  }

  def layout(scene: Scene): Unit = {
    scene.root foreach layoutWithParent(scene)
  }

  def layoutWithParent(parent: LayoutSize)(node: Node): Unit = {

    adjustSizeWithParent(parent)(node)

  }

  def adjustSizeWithParent(parent: LayoutSize)(node: Node): Unit = {
    node match {
      case group: Group with ParentRelatedSize => {
    	  group adjustSize parent
    	  group.children foreach adjustSizeWithParent(parent)
      }
      case node: ParentRelatedSize => node adjustSize parent
      case group: Group => adjustSize(group)
      case node => // no need to resize
    }
  }

  def adjustSize(node: Node): Unit = {
    node match {
      case node: ParentRelatedSize => throw new Error("Can not resize ParentRelatedSize nodes without a parent")
      case group: Group => adjustSize(group)
      case node => // no need to resize
    }
  }

  def adjustSize(group: Group): Unit = {

    val ((width, height), parentRelatedSizeNodes) =
      group match {
        case layout: Layout => getSizeInformation(group)(layout.totalChildSize)
        //default implementation without layout uses the largest child
        case group => getSizeInformation(group)(
          (node, width, height, nodeWidth, nodeHeight) =>
            math.max(width, nodeWidth) -> math.max(height, nodeHeight))
      }

    //update the size of the group itself
    implicit val access = RestrictedAccess

    group.width = width
    group.height = height

    //update the children that need the parent for their size
    parentRelatedSizeNodes foreach adjustSizeWithParent(group)
  }

  def getSizeInformation(group: Group)(
    totalChildSize: (Node, Width, Height, NodeWidth, NodeHeight) => Size): (Size, Seq[ParentRelatedSizeNode]) =
    /*
     * loop through the children and determine the size and type of the node
     * 
     * The result is stored in a pair containing the size and a list of parent 
     * related nodes. We need these so we can update them after we adjusted 
     * the size of the group
     */
    group.children.foldLeft((0d -> 0d) -> Seq[ParentRelatedSizeNode]()) {
      case (((width, height), parentRelatedSizeNodes), node) =>
        node match {
          case node: ParentRelatedSize =>
            (totalChildSize(node, width, height, node.minWidth, node.minHeight),
              parentRelatedSizeNodes :+ node)
          case node =>
            //before we get the size we need to adjust the size of the child
            adjustSize(node)
            (totalChildSize(node, width, height, node.width, node.height),
              parentRelatedSizeNodes)
        }
    }
}