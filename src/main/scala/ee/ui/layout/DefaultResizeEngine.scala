package ee.ui.layout

import ee.ui.traits.LayoutSize
import ee.ui.Node
import ee.ui.Group
import ee.ui.traits.RestrictedAccess
import scala.collection.immutable.Vector
import ee.ui.traits.LayoutWidth
import ee.ui.traits.LayoutHeight
import ee.ui.traits.ExplicitHeight
import ee.ui.traits.ExplicitSize
import ee.ui.traits.PartialExplicitSize
import ee.ui.traits.ExplicitWidth

//TODO build something so that only shizzle is measured if something has changed, 
//     also use LayoutClient.includeInLayout
//TODO maybe implement the commands as streams?
class DefaultResizeEngine {

  def adjustSizeWithParent(parent: LayoutSize, node: Node): Unit = {
    val commands = resizeWithParentSizeKnown(parent, node, determineSize)

    commands foreach (_.execute)
  }

  type ParentRelatedSize = ParentRelatedWidth with ParentRelatedHeight

  val noCommands = Vector.empty[Command]
  @inline def dontDetermineSize(node: Node) = noCommands
  @inline def dontDetermineWidth(parent: Group, node: Node) = noCommands
  @inline def dontDetermineHeight(parent: Group, node: Node) = noCommands
  @inline def dontResizeChildren(parent: Group, node: Node): Vector[Command] = noCommands

  def determineSize(node: Node): Vector[Command] =
    node match {
      case explicit: PartialExplicitSize => noCommands
      case parentRelated: PartialParentRelatedSize => noCommands
      case group: Group => determineGroupSize(group)
      case node => noCommands
    }

  def determineWidth(node: Node): Vector[Command] =
    node match {
      case explicit: ExplicitWidth => noCommands
      case parentRelated: ParentRelatedWidth => noCommands
      case group: Group => determineGroupWidth(group)(dontDetermineHeight)
      case node => noCommands
    }

  def determineHeight(node: Node): Vector[Command] =
    node match {
      case explicit: ExplicitHeight => noCommands
      case parentRelated: ParentRelatedHeight => noCommands
      case group: Group => determineGroupHeight(group)(dontDetermineWidth)
      case node => noCommands
    }

  def resizeWithParentSizeKnown(parent: LayoutSize, node: Node, determineSizeFunction: Node => Vector[Command]): Vector[Command] =
    node match {
      case group: Group with ParentRelatedSize => resizeGroup(group, parent)
      case group: Group with ParentRelatedWidth => resizeGroup(group, parent)
      case group: Group with ParentRelatedHeight => resizeGroup(group, parent)
      case node: ParentRelatedSize => Vector(ResizeBothCommand(node, parent))
      case node: ParentRelatedWidth => Vector(ResizeWidthCommand(node, parent))
      case node: ParentRelatedHeight => Vector(ResizeHeightCommand(node, parent))
      case nodeOrGroup => determineSizeFunction(nodeOrGroup)
    }

  def resizeWithParentWidthKnown(parent: LayoutWidth, node: Node, determineWidthFunction: Node => Vector[Command]): Vector[Command] =
    node match {
      case group: Group with ParentRelatedWidth => resizeGroup(group, parent)
      case node: ParentRelatedWidth => Vector(ResizeWidthCommand(node, parent))
      case nodeOrGroup => determineWidthFunction(nodeOrGroup)
    }

  def resizeWithParentHeightKnown(parent: LayoutHeight, node: Node, determineHeightFunction: Node => Vector[Command]): Vector[Command] =
    node match {
      case group: Group with ParentRelatedHeight => resizeGroup(group, parent)
      case node: ParentRelatedHeight => Vector(ResizeHeightCommand(node, parent))
      case nodeOrGroup => determineHeightFunction(nodeOrGroup)
    }

  trait Command {
    def execute(): Unit
  }

  case class ResizeBothCommand(node: ParentRelatedSize, parent: LayoutSize) extends Command {
    def execute() = {
      node adjustWidthTo parent
      node adjustHeightTo parent
    }
  }

  case class ResizeWidthCommand(node: ParentRelatedWidth, parent: LayoutWidth) extends Command {
    def execute() = node adjustWidthTo parent
  }

  case class ResizeHeightCommand(node: ParentRelatedHeight, parent: LayoutHeight) extends Command {
    def execute() = node adjustHeightTo parent
  }

  case class ResizeToChildrenCommand[T](
    childSizeDeterminationEntries: Seq[DetermineChildSize[T]],
    start: T,
    accumulationFunction: (T, T) => T,
    applyResult: T => Unit) extends Command {

    def execute() = {
      val result =
        (childSizeDeterminationEntries foldLeft start) { (acc, entry) =>

          accumulationFunction(acc, entry.size)
        }

      applyResult(result)
    }
  }

  case class DetermineChildSize[T](val commands: Vector[Command], retrieveSize: () => T) {
    def size: T = {
      commands foreach (_.execute)
      retrieveSize()
    }
  }

  def resizeGroup(group: Group with ParentRelatedSize, parent: LayoutSize): Vector[Command] = {
    ResizeBothCommand(group, parent) +:
      //since we have resized the group it's safe to create commands for all children
      (group.children foldLeft Vector[Command]()) { (commands, child) =>
        commands ++ resizeWithParentSizeKnown(group, child, determineSize)
      }
  }

  def resizeGroup(group: Group with ParentRelatedWidth, parent: LayoutWidth): Vector[Command] = {

    ResizeWidthCommand(group, parent) +:
      determineGroupHeight(group)(resizeWithParentWidthKnown(_: LayoutWidth, _: Node, determineWidth))
  }

  def resizeGroup(group: Group with ParentRelatedHeight, parent: LayoutHeight): Vector[Command] = {

    ResizeHeightCommand(group, parent) +:
      determineGroupWidth(group: Group)(resizeWithParentHeightKnown(_: LayoutHeight, _: Node, determineHeight))
  }

  @inline def getChildSize(node: Node): Size =
    node match {
      case node: ParentRelatedSize => (node.minWidth, node.minHeight)
      case node: ParentRelatedWidth => (node.minWidth, node.height)
      case node: ParentRelatedHeight => (node.width, node.minHeight)
      case node => (node.width, node.height)
    }

  @inline def getChildWidth(node: Node): Width =
    node match {
      case node: ParentRelatedWidth => node.minWidth
      case node => node.width
    }

  @inline def getChildHeight(node: Node): Height =
    node match {
      case node: ParentRelatedHeight => node.minHeight
      case node => node.height
    }

  def determineGroupSize(group: Group): Vector[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildren[Size](group)(
      start = (0d, 0d),
      accumulator = determineSizeAccumulator(group),
      childSize = getChildSize,
      applyResult = {
        case (width, height) =>
          group.width = width
          group.height = height
      })(
        directChildSizeModifications = dontResizeChildren,
        determineChildSizeFunction = determineSize,
        delayedChildSizeModifications = resizeWithParentSizeKnown(_: LayoutSize, _: Node, dontDetermineSize))
  }

  def determineGroupWidth(group: Group)(directChildHeightModifications: (Group, Node) => Vector[Command]): Vector[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildren[Width](group)(
      start = 0d,
      accumulator = determineWidthAccumulator(group),
      childSize = getChildWidth,
      applyResult = group.width_=)(
        directChildSizeModifications = directChildHeightModifications,
        determineChildSizeFunction = determineWidth,
        delayedChildSizeModifications = resizeWithParentWidthKnown(_: LayoutWidth, _: Node, dontDetermineSize))

  }

  def determineGroupHeight(group: Group)(directChildWidthModifications: (Group, Node) => Vector[Command]): Vector[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildren[Height](group)(
      start = 0d,
      accumulator = determineHeightAccumulator(group),
      childSize = getChildHeight,
      applyResult = group.height_=)(
        directChildSizeModifications = directChildWidthModifications,
        determineChildSizeFunction = determineHeight,
        delayedChildSizeModifications = resizeWithParentHeightKnown(_: LayoutHeight, _: Node, dontDetermineSize))
  }

  def resizeToChildren[T](group: Group)(
    start: T, accumulator: (T, T) => T, childSize: Node => T, applyResult: T => Unit)(
      directChildSizeModifications: (Group, Node) => Vector[Command], 
      determineChildSizeFunction: (Node) => Vector[Command], 
      delayedChildSizeModifications: (Group, Node) => Vector[Command]): Vector[Command] = {

    val children = group.children
    if (children.isEmpty) noCommands
    else {
      // these are the vectors we use to gather information while looping over the children
      val startInformation = (Vector[Command](), Vector[DetermineChildSize[T]](), Vector[Command]())

      val (directCommands, childSizeDeterminationEntries, afterSelfResizeCommands) =
        (children foldLeft startInformation) { (information, child) =>

          val (directCommands, childSizeDeterminationEntries, afterSelfResizeCommands) = information

          def childSizeWrapper() = childSize(child)

          (directCommands ++ directChildSizeModifications(group, child),
            childSizeDeterminationEntries :+ DetermineChildSize[T](determineChildSizeFunction(child), childSizeWrapper),
            afterSelfResizeCommands ++ delayedChildSizeModifications(group, child))
        }

      implicit val access = RestrictedAccess

      val resizeToChildrenCommand = ResizeToChildrenCommand[T](
        childSizeDeterminationEntries,
        start,
        accumulator,
        applyResult)

      (directCommands :+ resizeToChildrenCommand) ++ afterSelfResizeCommands
    }
  }

  def determineSizeAccumulator(group: Group): (Size, Size) => Size = {
    //retrieve outside of the returned function so they don't need to be called too often
    val widthAccumulator = determineWidthAccumulator(group)
    val heightAccumulator = determineHeightAccumulator(group)

    {
      case ((totalWidth, totalHeight), (nodeWidth, nodeHeight)) =>

        widthAccumulator(totalWidth, nodeWidth) -> heightAccumulator(totalHeight, nodeHeight)
    }
  }

  @inline def determineWidthAccumulator(group: Group): (Width, Width) => Width =
    group match {
      case layout: Layout => layout.determineTotalChildWidth
      case group => math.max
    }

  @inline def determineHeightAccumulator(group: Group): (Height, Height) => Height =
    group match {
      case layout: Layout => layout.determineTotalChildHeight
      case group => math.max
    }

}