package ee.ui.layout

import org.specs2.Specification
import ee.ui.Group
import ee.ui.Node
import language.postfixOps
import ee.ui.traits.RestrictedAccess
import ee.ui.traits.LayoutSize
import org.specs2.specification.Fragments
import ee.ui.traits.ExplicitSize
import ee.ui.traits.ExplicitHeight
import ee.ui.traits.ExplicitWidth
import ee.ui.traits.LayoutWidth
import ee.ui.traits.LayoutHeight
/**/
object DefaultResizeEngineInternalsSpecification extends Specification {

  val engine = new DefaultResizeEngine

  import engine._

  def is = "DefaultResizeEngine internals Specification".title ^
  //hide ^ end
  show ^ end

  def hide = "Specification is hidden" ^ end

  def show =
    "Unit tests on methods" ^
      """ accumulation methods
      
      	These methods are used to determine the size of a group based on it's 
      	children. The initial call is like acc(totalHeight = 0, nodeHeight = 100).
      	The default implementation simply returns the the largest of the two. 
      	When a Layout is present however, that should be used to determine the outcome.
      """ ^
      br ^
      { widthAccumulation(new TestGroup)(1, 2) must_== math.max(1, 2) } ^
      { widthAccumulation(new TestGroup with TestLayout)(1, 2) must_== 4 } ^
      { heightAccumulation(new TestGroup)(1, 2) must_== math.max(1, 2) } ^
      { heightAccumulation(new TestGroup with TestLayout)(1, 2) must_== 5 } ^
      { sizeAccumulation(new TestGroup)((1, 2), (3, 4)) must_== (math.max(1, 3), math.max(2, 4)) } ^
      { sizeAccumulation(new TestGroup with TestLayout)((2, 1), (1, 2)) must_== (4, 5) } ^
      p ^
      """ Determine child size methods
      
      	If a child depends on it's parent for size, we use it's minimal size
      """ ^
      br ^
      { determineChildSize(new TestNode { width = 1; height = 2 }) must_== (1, 2) } ^
      { determineChildSize(new TestNode with ParentRelatedSize { minWidth = 1; minHeight = 2 }) must_== (1, 2) } ^
      { determineChildWidth(new TestNode { width = 1 }) must_== 1 } ^
      { determineChildWidth(new TestNode with ParentRelatedSize { minWidth = 1 }) must_== 1 } ^
      { determineChildHeight(new TestNode { height = 1 }) must_== 1 } ^
      { determineChildHeight(new TestNode with ParentRelatedSize { minHeight = 1 }) must_== 1 } ^
      p ^
      """ Determine size command gathering
      
      	These commands are called with nodes as arguments, they however react only to 
      	groups. On top of that, they only react to groups that are not dependent on 
      	their parents for size. In short they resize groups to their children.
      """ ^
      br ^
      { determineSizeCommands(new TestNode) must be empty } ^
      { determineSizeCommands(new TestGroup) must be empty } ^
      { determineSizeCommands(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineSizeCommands(new TestGroup with ParentRelatedSize { children(new TestNode) }) must be empty } ^
      { determineSizeCommands(new TestGroup with ExplicitSize { children(new TestNode) }) must be empty } ^
      { determineWidthCommands(new TestNode) must be empty } ^
      { determineWidthCommands(new TestGroup) must be empty } ^
      { determineWidthCommands(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineWidthCommands(new TestGroup with ParentRelatedHeight { children(new TestNode) }) must not be empty } ^
      { determineWidthCommands(new TestGroup with ParentRelatedWidth { children(new TestNode) }) must be empty } ^
      { determineWidthCommands(new TestGroup with ExplicitHeight { children(new TestNode) }) must not be empty } ^
      { determineWidthCommands(new TestGroup with ExplicitWidth { children(new TestNode) }) must be empty } ^
      { determineHeightCommands(new TestNode) must be empty } ^
      { determineHeightCommands(new TestGroup) must be empty } ^
      { determineHeightCommands(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineHeightCommands(new TestGroup with ParentRelatedWidth { children(new TestNode) }) must not be empty } ^
      { determineHeightCommands(new TestGroup with ParentRelatedHeight { children(new TestNode) }) must be empty } ^
      { determineHeightCommands(new TestGroup with ExplicitWidth { children(new TestNode) }) must not be empty } ^
      { determineHeightCommands(new TestGroup with ExplicitHeight { children(new TestNode) }) must be empty } ^
      p ^
      """ Commands
      
      	These are the commands that will eventually be executed
      """ ^
      br ^
      { //ResizeBothCommand
        val node = new TestNode with ParentRelatedSize
        ResizeBothCommand(node, new TestGroup { width = 2; height = 4; }).execute
        (node.width.value must_== 1) and (node.height.value must_== 2)
      } ^
      { //ResizeWidthCommand
        val node = new TestNode with ParentRelatedSize
        ResizeWidthCommand(node, new TestGroup { width = 2; height = 4; }).execute
        (node.width.value must_== 1) and (node.height.value must_== 0)
      } ^
      { //ResizeHeightCommand
        val node = new TestNode with ParentRelatedSize
        ResizeHeightCommand(node, new TestGroup { width = 2; height = 4; }).execute
        (node.width.value must_== 0) and (node.height.value must_== 2)
      } ^
      { //AccumulatorEntry
        var result = false

        val command = TestCommand({ result = true })

        val value = AccumulatorEntry(commands = Vector(command), retrieveValue = () => 0).value

        (result must_== true) and (value must_== 0)
      } ^
      { //AccumulatorCommand
        val entries = Seq(AccumulatorEntry(commands = Vector.empty, retrieveValue = () => 2),
          AccumulatorEntry(commands = Vector.empty, retrieveValue = () => 3))

        var result = 0

        AccumulatorCommand[Int](accumulatorEntries = entries,
          start = 1,
          accumulationFunction = { _ + _ },
          applyResult = { result = _ }).execute

        result must_== 6
      } ^
      p ^
      """ Helper methods
      
      	These methods help with the readability of the other code and should return 
      	an empty vector.
      """ ^
      br ^
      { dontDetermineSizeCommands(new TestNode) must be empty } ^
      { dontDetermineWidthCommands(new TestGroup, new TestNode) must be empty } ^
      { dontDetermineHeightCommands(new TestGroup, new TestNode) must be empty } ^
      { parentSizeUnknownCommands(new TestGroup, new TestNode) must be empty } ^
      p ^
      """ Group determine size commands
      
      	The underlying method they use is `resizeToChildrenCommands`. This is quite a complex 
      	method. In order for me to keep sane the `groupDetermineXCommands` handle all use 
      	cases and `resizeToChildrenCommands` is never called outside of those methods. 
      """ ^
      br ^
      { //resizeToChildrenCommands
        val group = new TestGroup { children(new TestNode) }

        val childSize = 2
        val Def1 = (a: Int, b: Int) => 0
        val Def2 = (n: Node) => childSize
        val Def3 = (i: Int) => {}

        val commands = resizeToChildrenCommands[Int](group)(
          start = 1,
          accumulator = Def1,
          childSize = Def2,
          applyResult = Def3)(
            directChildSizeCommands = { (g, n) => Vector(NamedCommand("directChildSize")) },
            accumulatorSizeCommands = { n => Vector(NamedCommand("accumulateSizeCommand")) },
            delayedSizeCommands = { (g, n) => Vector(NamedCommand("delayedSizeCommand")) })

        commands must beLike {
          case Vector(
            NamedCommand("directChildSize"),
            AccumulatorCommand(Seq(
              AccumulatorEntry(Vector(NamedCommand("accumulateSizeCommand")), def0)),
              1, Def1, Def3),
            NamedCommand("delayedSizeCommand")) => def0() must_== childSize
        }
      } ^
      { //groupDetermineSizeCommands

        val group = new TestGroup {
          children(
            new TestNode,
            new TestNode with ParentRelatedSize)
        }

        val commands = groupDetermineSizeCommands(group)

        commands must beLike {
          case Vector(
            AccumulatorCommand(Seq(
              AccumulatorEntry(Vector(), _), AccumulatorEntry(Vector(), _)),
              _, _, _),
            ResizeBothCommand(_: TestNode with ParentRelatedSize, _)
            ) => ok
        }
      } ^
      { //groupDetermineWidthCommands

        val group = new TestGroup {
          children(
            new TestNode,
            new TestNode with ParentRelatedSize)
        }

        val commands = groupDetermineWidthCommands(group)(dontDetermineHeightCommands)

        commands must beLike {
          case Vector(
            AccumulatorCommand(Seq(
              AccumulatorEntry(Vector(), _), AccumulatorEntry(Vector(), _)),
              _, _, _),
            ResizeWidthCommand(_: TestNode with ParentRelatedSize, _)
            ) => ok
        }
      } ^ end

  case class NamedCommand(name: String) extends Command { def execute = {} }

  class TestCommand(command: => Unit) extends Command {
    def execute = command
  }
  object TestCommand {
    def apply(command: => Unit) = new TestCommand(command)
  }

  class TestNode extends Node with RestrictedAccess
  class TestGroup extends Group with RestrictedAccess

  trait ParentRelatedWidth extends ee.ui.layout.ParentRelatedWidth { self: Node =>
    def calculateWidth(parent: LayoutWidth): Width = parent.width / 2
  }
  trait ParentRelatedHeight extends ee.ui.layout.ParentRelatedHeight { self: Node =>
    def calculateHeight(parent: LayoutHeight): Height = parent.height / 2
  }

  trait ParentRelatedSize extends ParentRelatedWidth with ParentRelatedHeight { self: Node =>
  }

  trait TestLayout extends Layout { self: Group =>
    def calculateChildWidth(node: Node with ee.ui.layout.ParentRelatedWidth): Width = 2
    def calculateChildHeight(node: Node with ee.ui.layout.ParentRelatedHeight): Height = 3
    def determineTotalChildWidth(totalWidth: Double, nodeWidth: Double): Width = 4
    def determineTotalChildHeight(totalHeight: Double, nodeHeight: Double): Height = 5

    def updateLayout: Unit = {}
  }
}
/**/