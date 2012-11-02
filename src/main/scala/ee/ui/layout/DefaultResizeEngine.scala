package ee.ui.layout

import ee.ui.traits.LayoutSize
import ee.ui.Node
import ee.ui.Group
import ee.ui.traits.RestrictedAccess
import scala.collection.immutable.Stream
import ee.ui.traits.LayoutWidth
import ee.ui.traits.LayoutHeight
import ee.ui.traits.ExplicitHeight
import ee.ui.traits.ExplicitSize
import ee.ui.traits.PartialExplicitSize
import ee.ui.traits.ExplicitWidth

//TODO build something so that only shizzle is measured if something has changed, 
//     also use LayoutClient.includeInLayout
class DefaultResizeEngine {

  def adjustSizeWithParent(parent: LayoutSize, node: Node): Unit = {
    val commands = resizeWithParentSizeKnown(parent, node, determineSize)

    commands foreach (_.execute)
  }

  type ParentRelatedSize = ParentRelatedWidth with ParentRelatedHeight

  val noCommands = Stream.empty[Command]
  @inline def dontDetermineSize(node: Node) = noCommands
  @inline def dontDetermineWidth(parent: Group, node: Node) = noCommands
  @inline def dontDetermineHeight(parent: Group, node: Node) = noCommands
  @inline def dontResizeChildren(parent: Group, node: Node): Stream[Command] = noCommands

  def determineSize(node: Node): Stream[Command] =
    node match {
      case explicit: PartialExplicitSize => noCommands
      case parentRelated: PartialParentRelatedSize => noCommands
      case group: Group => determineGroupSize(group)
      case node => noCommands
    }

  def determineWidth(node: Node): Stream[Command] =
    node match {
      case explicit: ExplicitWidth => noCommands
      case parentRelated: ParentRelatedWidth => noCommands
      case group: Group => determineGroupWidth(group)(dontDetermineHeight)
      case node => noCommands
    }

  def determineHeight(node: Node): Stream[Command] =
    node match {
      case explicit: ExplicitHeight => noCommands
      case parentRelated: ParentRelatedHeight => noCommands
      case group: Group => determineGroupHeight(group)(dontDetermineWidth)
      case node => noCommands
    }

  def resizeWithParentSizeKnown(parent: LayoutSize, node: Node, determineSizeFunction: Node => Stream[Command]): Stream[Command] =
    node match {
      case group: Group with ParentRelatedSize => resizeGroup(group, parent)
      case group: Group with ParentRelatedWidth => resizeGroup(group, parent)
      case group: Group with ParentRelatedHeight => resizeGroup(group, parent)
      case node: ParentRelatedSize => Stream(ResizeBothCommand(node, parent))
      case node: ParentRelatedWidth => Stream(ResizeWidthCommand(node, parent))
      case node: ParentRelatedHeight => Stream(ResizeHeightCommand(node, parent))
      case nodeOrGroup => determineSizeFunction(nodeOrGroup)
    }

  def resizeWithParentWidthKnown(parent: LayoutWidth, node: Node, determineWidthFunction: Node => Stream[Command]): Stream[Command] =
    node match {
      case group: Group with ParentRelatedWidth => resizeGroup(group, parent)
      case node: ParentRelatedWidth => Stream(ResizeWidthCommand(node, parent))
      case nodeOrGroup => determineWidthFunction(nodeOrGroup)
    }

  def resizeWithParentHeightKnown(parent: LayoutHeight, node: Node, determineHeightFunction: Node => Stream[Command]): Stream[Command] =
    node match {
      case group: Group with ParentRelatedHeight => resizeGroup(group, parent)
      case node: ParentRelatedHeight => Stream(ResizeHeightCommand(node, parent))
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
    determineChildSizeFunction: () => T,
    applyResult: T => Unit) extends Command {

    def execute() = {
      val result =

        applyResult(determineChildSizeFunction())
    }
  }

  def resizeGroup(group: Group with ParentRelatedSize, parent: LayoutSize): Stream[Command] =
    ResizeBothCommand(group, parent) #::
      //since we have resized the group it's safe to create commands for all children
      (group.children foldLeft Stream[Command]()) { (commands, child) =>
        commands ++ resizeWithParentSizeKnown(group, child, determineSize)
      }

  def resizeGroup(group: Group with ParentRelatedWidth, parent: LayoutWidth): Stream[Command] = {

    ResizeWidthCommand(group, parent) #::
      determineGroupHeight(group)(resizeWithParentWidthKnown(_: LayoutWidth, _: Node, determineWidth))
  }

  def resizeGroup(group: Group with ParentRelatedHeight, parent: LayoutHeight): Stream[Command] = {

    ResizeHeightCommand(group, parent) #::
      determineGroupWidth(group: Group)(resizeWithParentHeightKnown(_: LayoutHeight, _: Node, determineHeight))
  }

  @inline def getChildSize(node: Node): Size =
    node match {
      case node: ParentRelatedSize => (node.minRequiredWidth, node.minRequiredHeight)
      case node: ParentRelatedWidth => (node.minRequiredWidth, node.height)
      case node: ParentRelatedHeight => (node.width, node.minRequiredHeight)
      case node => (node.width, node.height)
    }

  @inline def getChildWidth(node: Node): Width =
    node match {
      case node: ParentRelatedWidth => node.minRequiredWidth
      case node => node.width
    }

  @inline def getChildHeight(node: Node): Height =
    node match {
      case node: ParentRelatedHeight => node.minRequiredHeight
      case node => node.height
    }

  def updateGroupSize(group: Group)(size: Size): Unit = {
    implicit val access = RestrictedAccess
    val (width, height) = size
    group.width = width
    group.height = height
  }

  def updateGroupWidth(group: Group)(width: Width): Unit = {
    implicit val access = RestrictedAccess
    group.width = width
  }

  def updateGroupHeight(group: Group)(height: Height): Unit = {
    implicit val access = RestrictedAccess
    group.height = height
  }

  def determineGroupSize(group: Group): Stream[Command] = {

    resizeToChildren[Size](group)(
      determineChildSizeFunction = determineTotalChildSize(group),
      applyResult = updateGroupSize(group))(
        directChildSizeModifications = dontResizeChildren,
        delayedChildSizeModifications = resizeWithParentSizeKnown(_: LayoutSize, _: Node, dontDetermineSize))
  }

  def determineGroupWidth(group: Group)(directChildHeightModifications: (Group, Node) => Stream[Command]): Stream[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildren[Width](group)(
      determineChildSizeFunction = determineTotalChildWidth(group),
      applyResult = updateGroupWidth(group))(
        directChildSizeModifications = directChildHeightModifications,
        delayedChildSizeModifications = resizeWithParentWidthKnown(_: LayoutWidth, _: Node, dontDetermineSize))

  }

  def determineGroupHeight(group: Group)(directChildWidthModifications: (Group, Node) => Stream[Command]): Stream[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildren[Height](group)(
      determineChildSizeFunction = determineTotalChildHeight(group),
      applyResult = updateGroupHeight(group))(
        directChildSizeModifications = directChildWidthModifications,
        delayedChildSizeModifications = resizeWithParentHeightKnown(_: LayoutHeight, _: Node, dontDetermineSize))
  }

  def resizeToChildren[T](group: Group)(
    determineChildSizeFunction: () => T, applyResult: T => Unit)(
      directChildSizeModifications: (Group, Node) => Stream[Command],
      delayedChildSizeModifications: (Group, Node) => Stream[Command]): Stream[Command] = {

    val children = group.children
    if (children.isEmpty) noCommands
    else {
      // these are the Streams we use to gather information while looping over the children
      val startInformation = (Stream[Command](), Stream[Command]())

      val (directCommands, afterSelfResizeCommands) =
        (children foldLeft startInformation) { (information, child) =>

          val (directCommands, afterSelfResizeCommands) = information

          val newDirectCommands =
            directCommands ++ directChildSizeModifications(group, child)

          val newAfterSelfResizeCommands =
            afterSelfResizeCommands ++ delayedChildSizeModifications(group, child)

          (newDirectCommands, newAfterSelfResizeCommands)
        }

      implicit val access = RestrictedAccess

      val resizeToChildrenCommand = ResizeToChildrenCommand[T](
        determineChildSizeFunction,
        applyResult)

      directCommands ++ (resizeToChildrenCommand #:: afterSelfResizeCommands)
    }
  }

  def determineTotalChildSize(group: Group): () => Size = {
    val totalChildWidth = determineTotalChildWidth(group)
    val totalChildHeight = determineTotalChildHeight(group)

    { () => totalChildWidth() -> totalChildHeight() }
  }

  def determineTotalChildWidth(group: Group): () => Width = { () =>
    group match {
      case layout: Layout => layout.determineTotalChildWidth(getChildWidth)
      case group => Layout.determineTotalChildWidth(group, getChildWidth)
    }
  }

  def determineTotalChildHeight(group: Group): () => Height = { () =>
    group match {
      case layout: Layout => layout.determineTotalChildHeight(getChildHeight)
      case group => Layout.determineTotalChildHeight(group, getChildHeight)
    }
  }

}