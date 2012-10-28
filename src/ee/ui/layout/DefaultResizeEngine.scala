package ee.ui.layout

import ee.ui.traits.LayoutSize
import ee.ui.Node
import ee.ui.Group
import ee.ui.traits.RestrictedAccess
import scala.collection.immutable.Queue
class DefaultResizeEngine {

  implicit private def parentRelatedSize(node: ParentRelatedWidth with ParentRelatedHeight) =
    new ParentRelatedSize(node)

  implicit private def groupResizeHelper(group: Group) =
    new GroupResizeHelperBuilder(group)

  def adjustSizeWithParent(parent: LayoutSize, node: Node): Unit =
    adjustSizeWithParent(true)(parent)(node)
    
  def adjustSizeWithParent(allowChildBasedResize:Boolean)(parent: LayoutSize)(node: Node): Unit =
    node match {
      case group: Group with ParentRelatedWidth with ParentRelatedHeight => {
        group adjustSizeTo parent
        group call RESIZE_CHILDREN
      }
      case group: Group with ParentRelatedWidth => {
        group adjustWidthTo parent
        if (allowChildBasedResize) group adjust GROUP_HEIGHT and RESIZE_CHILDREN
      }
      case group: Group with ParentRelatedHeight => {
        group adjustHeightTo parent
        if (allowChildBasedResize) group adjust GROUP_WIDTH and RESIZE_CHILDREN
      }
      case node: ParentRelatedWidth with ParentRelatedHeight =>
        node adjustSizeTo parent
      case node: ParentRelatedWidth =>
        node adjustWidthTo parent
      case node: ParentRelatedHeight =>
        node adjustHeightTo parent
      case node =>
        adjustSizeWithoutParent(node)
    }

  protected def adjustSizeWithoutParent(node: Node): Unit =
    node match {
      case node: ParentRelatedWidth with ParentRelatedHeight =>
        throw new IllegalArgumentException("Can not resize a node that relies on it's parent for size, this method does not know the parent. There is a flaw in your resize logic")
      case group: Group with ParentRelatedWidth =>
        group adjust GROUP_HEIGHT and RESIZE_CHILDREN
      case group: Group with ParentRelatedHeight =>
        group adjust GROUP_WIDTH and RESIZE_CHILDREN
      case group: Group =>
        group adjust GROUP_SIZE and RESIZE_CHILDREN
      case node => // no need to resize, has probably explicit size

    }

  /*
   * The traits and objects only exist to help improve the readability 
   * of the adjustSizeWithParent method
   */
  private sealed trait SizeAdjustmentType extends RestrictedAccess {
    def update(group: Group)(width: Double, height: Double): Unit
  }
  private object GROUP_SIZE extends SizeAdjustmentType {
    def update(group: Group)(width: Double, height: Double): Unit = {
      group.width = width
      group.height = height
    }
  }
  private object GROUP_WIDTH extends SizeAdjustmentType {
    def update(group: Group)(width: Double, height: Double): Unit = {
      group.width = width
    }
  }
  private object GROUP_HEIGHT extends SizeAdjustmentType {
    def update(group: Group)(width: Double, height: Double): Unit = {
      group.height = height
    }
  }
  //this object exists purely for readability
  private object RESIZE_CHILDREN

  private class GroupResizeHelperBuilder(group: Group) {
    def adjust(sizeAdjustmentType: SizeAdjustmentType): GroupResizeHelper =
      new GroupResizeHelper(group, Some(sizeAdjustmentType))

    def call(readability: RESIZE_CHILDREN.type): Unit =
      new GroupResizeHelper(group, None).run
  }

  private class GroupResizeHelper(group: Group, sizeAdjustment: Option[SizeAdjustmentType]) {

    type TotalWidth = Double
    type NodeWidth = Double
    type SizeCalculator = (TotalWidth, NodeWidth) => TotalWidth

    def defaultSizeCalculator: SizeCalculator = math.max

    class ChildUpdater(childUpdates: Queue[LayoutSize => Unit] = Queue.empty) extends Function0[Unit] {
      def apply(): Unit = 
        childUpdates foreach (childUpdate => childUpdate(group))

      def :+(childUpdate: LayoutSize => Unit): ChildUpdater =
        new ChildUpdater(childUpdates :+ childUpdate)
    }

    def run: Unit = {

      val children = group.children

      val updateChildren: () => Unit =
        /*
          If we have a size adjustment we need to size (part) of the group 
          to it's children. 
         */
        sizeAdjustment map { sizeAdjustment =>

          /*
           	Groups that have a Layout need to be treated different. The layout 
           	determines the total width and height of the children. Think of 
           	the HorizontalLayout for example. As a default we use the biggest 
           	width and height of a child.
           */
          lazy val (totalChildWidthCalculator, totalChildHeightCalculator) =
            group match {
              case layout: Layout => layout.updateTotalChildWidth _ -> layout.updateTotalChildHeight _
              case group => defaultSizeCalculator -> defaultSizeCalculator
            }

          type Information = (ChildUpdater, Width, Height)

          def sizeAndUpdate(information: Information, node: Node): Information = {
            val ( /* childUpdater */ cu, width, height) = information

            val tw = totalChildWidthCalculator(width, _:Double)
            val th = totalChildHeightCalculator(height, _:Double)
            val sizeUpdate = adjustSizeWithParent(false)(_:LayoutSize)(node)
            val sizeUpdateWithChildren = adjustSizeWithParent(true)(_:LayoutSize)(node)
            
            //TODO not too happy about this, maybe I can refactor it
            (sizeAdjustment, node) match {
              case (GROUP_SIZE, node: ParentRelatedWidth with ParentRelatedHeight) =>
                (cu :+ sizeUpdate, tw(node.minWidth), th(node.minHeight))
              case (GROUP_SIZE, node: ParentRelatedWidth) =>
                adjustSizeWithoutParent(node)
                (cu :+ sizeUpdate, tw(node.minWidth), th(node.height))
              case (GROUP_SIZE, node: ParentRelatedHeight) =>
                adjustSizeWithoutParent(node)
                (cu :+ sizeUpdate, tw(node.width), th(node.minHeight))
              case (GROUP_SIZE, node) =>
                adjustSizeWithoutParent(node)
                (cu, tw(node.width), th(node.height))
              case (GROUP_WIDTH, node: ParentRelatedWidth with ParentRelatedHeight) =>
                (cu :+ sizeUpdateWithChildren, tw(node.minWidth), height)
              case (GROUP_WIDTH, node: ParentRelatedWidth) =>
                (cu :+ sizeUpdateWithChildren, tw(node.minWidth), height)
              case (GROUP_WIDTH, node: ParentRelatedHeight) =>
                adjustSizeWithoutParent(node)
                (cu :+ sizeUpdate, tw(node.width), height)
              case (GROUP_WIDTH, node) =>
                adjustSizeWithoutParent(node)
                (cu, tw(node.width), height)
              case (GROUP_HEIGHT, node: ParentRelatedWidth with ParentRelatedHeight) =>
                (cu :+ sizeUpdateWithChildren, width, th(node.minHeight))
              case (GROUP_HEIGHT, node: ParentRelatedWidth) =>
                adjustSizeWithoutParent(node)
                (cu :+ sizeUpdate, width, th(node.height))
              case (GROUP_HEIGHT, node: ParentRelatedHeight) =>
                (cu :+ sizeUpdateWithChildren, width, th(node.minHeight))
              case (GROUP_HEIGHT, node) =>
                adjustSizeWithoutParent(node)
                (cu, width, th(node.height))
            }
          }
          /*
        	We need to determine the width, the height or both based on the children.
        	
        	We need to loop through all children, we will store the children that 
        	require the size of their parent (this group) for their own size. The 
        	other children are updated within this loop. 
          */
          val startInformation = (new ChildUpdater, 0d, 0d)
          val (childUpdater, width, height) =
            (children foldLeft startInformation)(sizeAndUpdate)

          //perform the correct size adjustment
          (sizeAdjustment update group)(width, height)

          childUpdater

          //if no size adjustment has taken place we want to adjust the size of all children
        } getOrElse adjustSizeForChildren(children, adjustSizeWithParent(true)(group))

      updateChildren()
    }

    def adjustSizeForChildren(children: Group#Children, adjustMethod: Node => Unit)() =
      children foreach adjustMethod

    def and(readability: RESIZE_CHILDREN.type): Unit =
      run

  }

  private class ParentRelatedSize(node: ParentRelatedWidth with ParentRelatedHeight) {

    def adjustSizeTo(parent: LayoutSize): Unit = {
      node adjustWidthTo parent
      node adjustHeightTo parent
    }

    def calculateSize(parent: LayoutSize): Size =
      (node calculateWidth parent, node calculateHeight parent)

  }
}