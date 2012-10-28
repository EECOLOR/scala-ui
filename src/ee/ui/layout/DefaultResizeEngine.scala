package ee.ui.layout

import ee.ui.traits.LayoutSize
import ee.ui.Node
import ee.ui.Group
import ee.ui.traits.RestrictedAccess
import scala.collection.immutable.Vector
import ee.ui.traits.LayoutWidth
import ee.ui.traits.LayoutHeight

//TODO build something so that only shizzle is measured if something has changed 
class DefaultResizeEngine {

  def adjustSizeWithParent(parent: LayoutSize, node: Node): Unit = {
    val commands = parentSizeKnownCommands(parent, node, determineSizeCommands)

    commands foreach (_.execute)
  }

  type ParentRelatedSize = ParentRelatedWidth with ParentRelatedHeight

  @inline def dontDetermineSizeCommands(node: Node) = Vector.empty[Command]
  @inline def dontDetermineWidthCommands(parent: Group, node: Node) = dontDetermineSizeCommands(node)
  @inline def dontDetermineHeightCommands(parent: Group, node: Node) = dontDetermineSizeCommands(node)

  def determineSizeCommands(node: Node): Vector[Command] =
    node match {
      case both: PartialParentRelatedSize => Vector.empty
      case group: Group => groupDetermineSizeCommands(group)
      case node => Vector.empty
    }

  def determineWidthCommands(node: Node): Vector[Command] =
    node match {
      case both: PartialParentRelatedSize => Vector.empty
      case group: Group => groupDetermineWidthCommands(group)(dontDetermineHeightCommands)
      case node => Vector.empty
    }

  def determineHeightCommands(node: Node): Vector[Command] =
    node match {
      case group: PartialParentRelatedSize => Vector.empty
      case group: Group => groupDetermineHeightCommands(group)(dontDetermineWidthCommands)
      case node => Vector.empty
    }

  def parentSizeKnownCommands(parent: LayoutSize, node: Node, determineSizeCommands: Node => Vector[Command]): Vector[Command] =
    node match {
      case group: Group with ParentRelatedSize => groupCommands(group, parent)
      case group: Group with ParentRelatedWidth => groupCommands(group, parent)
      case group: Group with ParentRelatedHeight => groupCommands(group, parent)
      case node: ParentRelatedSize => Vector(ResizeBothCommand(node, parent))
      case node: ParentRelatedWidth => Vector(ResizeWidthCommand(node, parent))
      case node: ParentRelatedHeight => Vector(ResizeHeightCommand(node, parent))
      case nodeOrGroup => determineSizeCommands(nodeOrGroup)
    }

  def parentWidthKnownCommands(parent: LayoutWidth, node: Node, determineWidthCommands: Node => Vector[Command]): Vector[Command] =
    node match {
      case group: Group with ParentRelatedWidth => groupCommands(group, parent)
      case node: ParentRelatedWidth => Vector(ResizeWidthCommand(node, parent))
      case nodeOrGroup => determineWidthCommands(nodeOrGroup)
    }

  def parentHeightKnownCommands(parent: LayoutHeight, node: Node, determineHeightCommands: Node => Vector[Command]): Vector[Command] =
    node match {
      case group: Group with ParentRelatedHeight => groupCommands(group, parent)
      case node: ParentRelatedHeight => Vector(ResizeHeightCommand(node, parent))
      case nodeOrGroup => determineHeightCommands(nodeOrGroup)
    }

  def parentSizeUnknownCommands(parent: Group, node: Node): Vector[Command] = Vector.empty

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

  case class AccumulatorCommand[T](
    accumulatorEntries: Seq[AccumulatorEntry[T]],
    start: T,
    accumulationFunction: (T, T) => T,
    applyResult: T => Unit) extends Command {

    def execute() = {
      val result =
        (accumulatorEntries foldLeft start) { (acc, entry) =>

          entry.commands foreach (_.execute)
          accumulationFunction(acc, entry.value)
        }

      applyResult(result)
    }
  }

  class AccumulatorEntry[T](val commands: Vector[Command], retrieveValue: => T) {
    def value: T = retrieveValue
  }

  object AccumulatorEntry {
    def apply[T](commands: Vector[Command], retrieveValue: => T) = new AccumulatorEntry(commands, retrieveValue)
  }

  def groupCommands(group: Group with ParentRelatedSize, parent: LayoutSize): Vector[Command] = {
    ResizeBothCommand(group, parent) +:
      //since we have resized the group it's safe to create commands for all children
      (group.children foldLeft Vector[Command]()) { (commands, child) =>
        commands ++ parentSizeKnownCommands(parent, child, determineSizeCommands)
      }
  }

  def groupCommands(group: Group with ParentRelatedWidth, parent: LayoutWidth): Vector[Command] = {

    ResizeWidthCommand(group, parent) +:
      groupDetermineHeightCommands(group)(parentWidthKnownCommands(_: LayoutWidth, _: Node, determineWidthCommands))
  }

  def groupCommands(group: Group with ParentRelatedHeight, parent: LayoutHeight): Vector[Command] = {

    ResizeHeightCommand(group, parent) +:
      groupDetermineWidthCommands(group: Group)(parentHeightKnownCommands(_: LayoutHeight, _: Node, determineHeightCommands))
  }

  def groupDetermineSizeCommands(group: Group): Vector[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildrenCommands[Size](group)(
      start = (0d, 0d),
      accumulator = sizeAccumulation(group),
      childSize = { child => (child.width, child.height) },
      applyResult = {
        case (width, height) =>
          group.width = width
          group.height = height
      })(
        directChildSizeCommands = parentSizeUnknownCommands,
        accumulatorSizeCommands = determineSizeCommands,
        delayedSizeCommands = parentSizeKnownCommands(_: LayoutSize, _: Node, dontDetermineSizeCommands))
  }

  def groupDetermineWidthCommands(group: Group)(parentHeightKnownCommands: (Group, Node) => Vector[Command]): Vector[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildrenCommands[Width](group)(
      start = 0d,
      accumulator = widthAccumulation(group),
      childSize = _.width,
      applyResult = group.width_=)(
        directChildSizeCommands = parentHeightKnownCommands,
        accumulatorSizeCommands = determineWidthCommands,
        delayedSizeCommands = parentWidthKnownCommands(_: LayoutWidth, _: Node, dontDetermineSizeCommands))

  }

  def groupDetermineHeightCommands(group: Group)(parentWidthKnownCommands: (Group, Node) => Vector[Command]): Vector[Command] = {
    implicit val access = RestrictedAccess

    resizeToChildrenCommands[Height](group)(
      start = 0d,
      accumulator = heightAccumulation(group),
      childSize = _.height,
      applyResult = group.height_=)(
        directChildSizeCommands = parentWidthKnownCommands,
        accumulatorSizeCommands = determineHeightCommands,
        delayedSizeCommands = parentHeightKnownCommands(_: LayoutHeight, _: Node, dontDetermineSizeCommands))
  }

  def resizeToChildrenCommands[T](group: Group)(
    start: T, accumulator: (T, T) => T, childSize: Node => T, applyResult: T => Unit)(
      directChildSizeCommands: (Group, Node) => Vector[Command],
      accumulatorSizeCommands: (Node) => Vector[Command],
      delayedSizeCommands: (Group, Node) => Vector[Command]): Vector[Command] = {

    // these are the vectors we use to gather information while looping over the children
    val startInformation = (Vector[Command](), Vector[AccumulatorEntry[T]](), Vector[Command]())

    val (directCommands, accumulatorEntries, afterSelfResizeCommands) =
      (group.children foldLeft startInformation) { (information, child) =>

        val (directCommands, accumulatorEntries, afterSelfResizeCommands) = information

        (directCommands ++ parentWidthKnownCommands(group, child, determineWidthCommands),
          accumulatorEntries :+ AccumulatorEntry[T](determineHeightCommands(child), childSize(child)),
          afterSelfResizeCommands ++ parentHeightKnownCommands(group, child, dontDetermineSizeCommands))
      }

    implicit val access = RestrictedAccess

    val accumulateAndResizeCommand = AccumulatorCommand[T](
      accumulatorEntries,
      start,
      accumulator,
      applyResult)

    (directCommands :+ accumulateAndResizeCommand) ++ afterSelfResizeCommands
  }

  def sizeAccumulation(group: Group): (Size, Size) => Size = {
    //retrieve outside of the returned method so they don't need to be called too often
    val wAccumulation = widthAccumulation(group)
    val hAccumulation = heightAccumulation(group)

    {
      case ((totalWidth, totalHeight), (nodeWidth, nodeHeight)) =>

        wAccumulation(totalWidth, nodeWidth) -> wAccumulation(totalHeight, nodeHeight)
    }
  }

  @inline def widthAccumulation(group: Group): (Width, Width) => Width =
    group match {
      case layout: Layout => layout.determineTotalChildWidth
      case group => math.max
    }

  @inline def heightAccumulation(group: Group): (Height, Height) => Height =
    group match {
      case layout: Layout => layout.determineTotalChildHeight
      case group => math.max
    }

}