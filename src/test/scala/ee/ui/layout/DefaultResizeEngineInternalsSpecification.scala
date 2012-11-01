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
      """ accumulator methods
      
      	These methods are used to determine the size of a group based on it's 
      	children. The initial call is like acc(totalHeight = 0, nodeHeight = 100).
      	The default implementation simply returns the the largest of the two. 
      	When a Layout is present however, that should be used to determine the outcome.
      """ ^
      br ^
      { determineWidthAccumulator(new TestGroup)(1, 2) must_== math.max(1, 2) } ^
      { determineWidthAccumulator(new TestGroup with TestLayout)(1, 2) must_== 4 } ^
      { determineHeightAccumulator(new TestGroup)(1, 2) must_== math.max(1, 2) } ^
      { determineHeightAccumulator(new TestGroup with TestLayout)(1, 2) must_== 5 } ^
      { determineSizeAccumulator(new TestGroup)((1, 2), (3, 4)) must_== (math.max(1, 3), math.max(2, 4)) } ^
      { determineSizeAccumulator(new TestGroup with TestLayout)((2, 1), (1, 2)) must_== (4, 5) } ^
      p ^
      """ Determine child size methods
      
      	If a child depends on it's parent for size, we use it's minimal size
      """ ^
      br ^
      { getChildSize(new TestNode { width = 1; height = 2 }) must_== (1, 2) } ^
      { getChildSize(new TestNode with ParentRelatedSize { minWidth = 1; minHeight = 2 }) must_== (1, 2) } ^
      { getChildSize(new TestNode with ParentRelatedWidth { minWidth = 1; height = 2 }) must_== (1, 2) } ^
      { getChildSize(new TestNode with ParentRelatedHeight { width = 1; minHeight = 2 }) must_== (1, 2) } ^
      { getChildWidth(new TestNode { width = 1 }) must_== 1 } ^
      { getChildWidth(new TestNode with ParentRelatedSize { minWidth = 1 }) must_== 1 } ^
      { getChildHeight(new TestNode { height = 1 }) must_== 1 } ^
      { getChildHeight(new TestNode with ParentRelatedSize { minHeight = 1 }) must_== 1 } ^
      p ^
      """ Determine size methods
      
      	These commands are called with nodes as arguments, they however react only to 
      	groups. On top of that, they only react to groups that are not dependent on 
      	their parents for size. In short they resize groups to their children.
      """ ^
      br ^
      { determineSize(new TestNode) must be empty } ^
      { determineSize(new TestGroup) must be empty } ^
      { determineSize(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineSize(new TestGroup with ParentRelatedSize { children(new TestNode) }) must be empty } ^
      { determineSize(new TestGroup with ExplicitSize { children(new TestNode) }) must be empty } ^
      { determineWidth(new TestNode) must be empty } ^
      { determineWidth(new TestGroup) must be empty } ^
      { determineWidth(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineWidth(new TestGroup with ParentRelatedHeight { children(new TestNode) }) must not be empty } ^
      { determineWidth(new TestGroup with ParentRelatedWidth { children(new TestNode) }) must be empty } ^
      { determineWidth(new TestGroup with ExplicitHeight { children(new TestNode) }) must not be empty } ^
      { determineWidth(new TestGroup with ExplicitWidth { children(new TestNode) }) must be empty } ^
      { determineHeight(new TestNode) must be empty } ^
      { determineHeight(new TestGroup) must be empty } ^
      { determineHeight(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineHeight(new TestGroup with ParentRelatedWidth { children(new TestNode) }) must not be empty } ^
      { determineHeight(new TestGroup with ParentRelatedHeight { children(new TestNode) }) must be empty } ^
      { determineHeight(new TestGroup with ExplicitWidth { children(new TestNode) }) must not be empty } ^
      { determineHeight(new TestGroup with ExplicitHeight { children(new TestNode) }) must be empty } ^
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
      { //DetermineChildSize
        var result = false

        val command = TestCommand({ result = true })

        val value = DetermineChildSize(commands = Stream(command), retrieveSize = () => 0).size

        (result must_== true) and (value must_== 0)
      } ^
      { //ResizeToChildrenCommand
        val entries = Stream(DetermineChildSize(commands = Stream.empty, retrieveSize = () => 2),
          DetermineChildSize(commands = Stream.empty, retrieveSize = () => 3))

        var result = 0

        ResizeToChildrenCommand[Int](childSizeDeterminationEntries = entries,
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
      { dontDetermineSize(new TestNode) must be empty } ^
      { dontDetermineWidth(new TestGroup, new TestNode) must be empty } ^
      { dontDetermineHeight(new TestGroup, new TestNode) must be empty } ^
      { dontResizeChildren(new TestGroup, new TestNode) must be empty } ^
      p ^
      """ Determine group size
      
      	The underlying method they use is `resizeToChildren`. This is quite a complex 
      	method. In order for me to keep sane the `determineGroupX` handle all use 
      	cases and `resizeToChildren` is never called outside of those methods. 
      """ ^
      br ^
      { //resizeToChildren
        val group = new TestGroup { children(new TestNode) }

        val childSize = 2
        val Def1 = (a: Int, b: Int) => 0
        val Def2 = (n: Node) => childSize
        val Def3 = (i: Int) => {}

        val commands = resizeToChildren[Int](group)(
          start = 1,
          accumulator = Def1,
          childSize = Def2,
          applyResult = Def3)(
            directChildSizeModifications = { (g, n) => Stream(NamedCommand("directChildSize")) },
            determineChildSizeFunction = { n => Stream(NamedCommand("accumulateSizeCommand")) },
            delayedChildSizeModifications = { (g, n) => Stream(NamedCommand("delayedSizeCommand")) })

        commands must beLike {
          case Stream(
            NamedCommand("directChildSize"),
            ResizeToChildrenCommand(Seq(
              DetermineChildSize(Stream(NamedCommand("accumulateSizeCommand")), def0)),
              1, Def1, Def3),
            NamedCommand("delayedSizeCommand")) => def0() must_== childSize
        }
      } ^
      { //determineGroupSize

        val group = new TestGroup {
          children(
            new TestNode,
            new TestNode with ParentRelatedSize)
        }

        val commands = determineGroupSize(group)

        commands must beLike {
          case Stream(
            ResizeToChildrenCommand(Seq(
              DetermineChildSize(Stream(), _), DetermineChildSize(Stream(), _)),
              _, _, _),
              ResizeBothCommand(_: TestNode with ParentRelatedSize, _)
            ) => ok
        }
      } ^
      { //determineGroupWidth

        val group = new TestGroup {
          children(
            new TestNode,
            new TestNode with ParentRelatedSize)
        }

        val commands = determineGroupWidth(group)(dontDetermineHeight)

        commands must beLike {
          case Stream(
            ResizeToChildrenCommand(Seq(
              DetermineChildSize(Stream(), _), DetermineChildSize(Stream(), _)),
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