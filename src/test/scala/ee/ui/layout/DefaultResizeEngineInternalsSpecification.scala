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
      """ total child sizes
      
      	These methods are used to determine the size of a group based on it's 
      	children. The default implementation simply returns the size of the largest 
    	child. When a Layout is present however, that should be used to determine 
    	the outcome.
      """ ^
      br ^
      {
        val group = new TestGroup {
          children(new TestNode { width = 1 }, new TestNode { width = 2 })
        }
        determineTotalChildWidth(group)() must_== math.max(1, 2)
      } ^
      {
        val group = new TestGroup with TestLayout {
          children(new TestNode { width = 1 }, new TestNode { width = 2 })
        }
        determineTotalChildWidth(group)() must_== 4
      } ^
      {
        val group = new TestGroup {
          children(new TestNode { height = 1 }, new TestNode { height = 2 })
        }
        determineTotalChildHeight(group)() must_== math.max(1, 2)
      } ^
      {
        val group = new TestGroup with TestLayout {
          children(new TestNode { height = 1 }, new TestNode { height = 2 })
        }
        determineTotalChildHeight(group)() must_== 5
      } ^
      {
        val group = new TestGroup {
          children(
            new TestNode { width = 1; height = 3 },
            new TestNode { width = 2; height = 4 })
        }
        determineTotalChildSize(group)() must_== (math.max(1, 2), math.max(3, 4))
      } ^
      {
        val group = new TestGroup with TestLayout {
          children(
            new TestNode { width = 1; height = 3 },
            new TestNode { width = 2; height = 4 })
        }
        determineTotalChildSize(group)() must_== (4, 5)
      } ^
      p ^
      """ Determine child size methods
      
      	If a child depends on it's parent for size, we use it's minimal size
      """ ^
      br ^
      { getChildSize(new TestNode { width = 1; height = 2 }) must_== (1, 2) } ^
      { getChildSize(new TestNode with ParentRelatedSize { minWidth = 1; minHeight = 2 }) must_== (1, 2) } ^
      { getChildSize(new TestNode with ParentRelatedWidth { minWidth = 1; height = 2 }) must_== (1, 2) } ^
      { getChildSize(new TestNode with ParentRelatedHeight { width = 1; minHeight = 2 }) must_== (1, 2) } ^
      {
        getChildSize(new TestNode with ParentRelatedSize {
          minWidth = 1
          minHeight = 2
          override def minRequiredWidth = 1d + minWidth
          override def minRequiredHeight = 2d + minHeight
        }) must_== (2, 4)
      } ^
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
      { //ResizeToChildrenCommand

        val determineChildSizeFunction = () => 8
        var result = 0

        ResizeToChildrenCommand[Int](
          determineChildSizeFunction,
          applyResult = { result = _ }).execute

        result must_== 8

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
        val Def1 = () => childSize
        val Def2 = (i: Int) => {}

        val commands = resizeToChildren[Int](group)(
          determineChildSizeFunction = Def1,
          applyResult = Def2)(
            directChildSizeModifications = { (g, n) => Stream(NamedCommand("directChildSize")) },
            delayedChildSizeModifications = { (g, n) => Stream(NamedCommand("delayedSizeCommand")) })

        commands must beLike {
          case Stream(
            NamedCommand("directChildSize"),
            ResizeToChildrenCommand(Def1, Def2),
            NamedCommand("delayedSizeCommand")) => ok
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
            ResizeToChildrenCommand(_, _),
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
            ResizeToChildrenCommand(_, _),
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
    def childrenResized(): Unit = {}

    def calculateChildWidth(node: Node with ee.ui.layout.ParentRelatedWidth): Width = 2
    def calculateChildHeight(node: Node with ee.ui.layout.ParentRelatedHeight): Height = 3
    def determineTotalChildWidth(getChildWidth: Node => Width): Width = 4
    def determineTotalChildHeight(getChildHeight: Node => Height): Height = 5

    def updateLayout: Unit = {}
  }
}
/**/