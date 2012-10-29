package ee.ui.layout

import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.Node
import ee.ui.traits.LayoutSize
import ee.ui.traits.RestrictedAccess

class DefaultLayoutEngine extends LayoutEngine {
  
  val resizeEngine = new DefaultResizeEngine
  
  def layout(stage: Stage): Unit = {
    stage.scene foreach layout
  }

  def layout(scene: Scene): Unit = {
    scene.root foreach layoutWithParent(scene)
  }

  def layoutWithParent(parent: LayoutSize)(node: Node): Unit = {

    resizeEngine.adjustSizeWithParent(parent, node)

  }
/*
  def adjustWidthWithParent(parent: LayoutSize)(node: Node): Unit = {
    node match {
      case group: Group with ParentRelatedWidth => {
        group adjustWidth parent
        group.children foreach adjustSizeWithParent(parent)
      }
      case node: ParentRelatedWidth => node adjustWidth parent
      case group: Group => adjustWidth(group)
      case node => // no need to resize
    }
  }

  def adjustHeightWithParent(parent: LayoutSize)(node: Node): Unit = {
    node match {
      case group: Group with ParentRelatedHeight => {
        group adjustHeight parent
        group.children foreach adjustSizeWithParent(parent)
      }
      case node: ParentRelatedHeight => node adjustHeight parent
      case group: Group => adjustHeight(group)
      case node => // no need to resize
    }
  }

  def adjustSizeWithParent(parent: LayoutSize)(node: Node): Unit = {
    node match {
      case group: Group with ParentRelatedWidth with ParentRelatedHeight => {
        group adjustWidth parent
        group adjustHeight parent
        group.children foreach adjustSizeWithParent(parent)
      }
      case group: Group with ParentRelatedWidth => {
        group adjustWidth parent
        adjustGroupHeight(group)
        group.children foreach adjustWidthWithParent(parent)
      }
      case group: Group with ParentRelatedHeight => {
        group adjustHeight parent
        adjustGroupWidth(group)
        group.children foreach adjustHeightWithParent(parent)
      }
      case node: ParentRelatedWidth with ParentRelatedHeight => node adjustSize parent
      case group: Group => adjustGroupSize(group)
      case node => // no need to resize
    }
  }

  def adjustSize(node: Node): Unit = {
    node match {
      case node: ParentRelatedWidth with ParentRelatedHeight => throw new Error("Can not resize ParentRelatedSize nodes without a parent")
      case group: Group => adjustGroupSize(group)
      case node => // no need to resize
    }
  }

  def adjustHeight(node: Node): Unit = {
    node match {
      case node: ParentRelatedHeight => throw new Error("Can not adjust height of ParentRelatedHeight nodes without a parent")
      case group: Group => adjustGroupHeight(group)
      case node => // no need to resize
    }
  }

  def adjustWidth(node: Node): Unit = {
    node match {
      case node: ParentRelatedWidth => throw new Error("Can not adjust width of ParentRelatedWidth nodes without a parent")
      case group: Group => adjustGroupWidth(group)
      case node => // no need to resize
    }
  }

  def adjustGroupHeight(group: Group): Unit = {

    val (height, parentRelatedHeightNodes) =
      getHeightInformation(group)(
        group match {
          case layout: Layout => layout.totalChildHeight
          //default implementation without layout uses the largest child
          case group => (node, height, nodeHeight) => math.max(height, nodeHeight)
        })

    //update the size of the group itself
    implicit val access = RestrictedAccess

    group.height = height

    //update the children that need the parent for their size
    parentRelatedHeightNodes foreach adjustHeightWithParent(group)
  }

  def adjustGroupWidth(group: Group): Unit = {

    val (width, parentRelatedWidthNodes) =
      getWidthInformation(group)(
        group match {
          case layout: Layout => layout.totalChildWidth
          //default implementation without layout uses the largest child
          case group => (node, height, nodeHeight) => math.max(height, nodeHeight)
        })

    //update the size of the group itself
    implicit val access = RestrictedAccess

    group.width = width

    //update the children that need the parent for their size, 
    //all other children have been resized in getWidthInformation 
    parentRelatedWidthNodes foreach adjustWidthWithParent(group)
  }

  def adjustGroupSize(group: Group): Unit = {

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

  def getHeightInformation(group: Group)(
    totalChildHeight: (Node, Height, NodeHeight) => Height): (Height, Seq[Node with ParentRelatedHeight]) =

    group.children.foldLeft(0d -> Seq[Node with ParentRelatedHeight]()) {
      case ((height, parentRelatedHeightNodes), node) =>
        node match {
          case node: ParentRelatedHeight =>
            (totalChildHeight(node, height, node.minHeight),
              parentRelatedHeightNodes :+ node)
          case node =>
            //before we get the size we need to adjust the size of the child
            adjustHeight(node)
            (totalChildHeight(node, height, node.height),
              parentRelatedHeightNodes)
        }
    }

  def getWidthInformation(group: Group)(
    totalChildWidth: (Node, Height, NodeHeight) => Height): (Height, Seq[Node with ParentRelatedWidth]) =

    group.children.foldLeft(0d -> Seq[Node with ParentRelatedWidth]()) {
      case ((width, parentRelatedWidthNodes), node) =>
        node match {
          case node: ParentRelatedWidth =>
            (totalChildWidth(node, width, node.minWidth),
              parentRelatedWidthNodes :+ node)
          case node =>
            //before we get the size we need to adjust the size of the child
            adjustWidth(node)
            (totalChildWidth(node, width, node.width),
              parentRelatedWidthNodes)
        }
    }

  def getSizeInformation(group: Group)(
    totalChildSize: (Node, Width, Height, NodeWidth, NodeHeight) => Size): (Size, Seq[Node with ParentRelatedWidth with ParentRelatedHeight]) =

    group.children.foldLeft((0d -> 0d) -> Seq[Node with ParentRelatedWidth with ParentRelatedHeight]()) {
      case (((width, height), parentRelatedSizeNodes), node) =>
        node match {
          case node: ParentRelatedWidth with ParentRelatedHeight =>
            (totalChildSize(node, width, height, node.minWidth, node.minHeight),
              parentRelatedSizeNodes :+ node)
          case node =>
            //before we get the size we need to adjust the size of the child
            adjustSize(node)
            (totalChildSize(node, width, height, node.width, node.height),
              parentRelatedSizeNodes)
        }
    }
    */
}