package ee.ui.layout

import org.specs2.Specification
import ee.ui.Group
import ee.ui.Node
import language.postfixOps
import ee.ui.traits.RestrictedAccess

object DefaultResizeEngineSpecification extends Specification {
  "DefaultResizeEngine Specification".title

  val engine = new DefaultResizeEngine

  val --- = null //only here to fix a printing bug in specs2

  import engine._

  def is =
    "Unit tests on methods" ^
      {
        ---
        "multiline example"
        "is fixed with"
        "the ---"
        success
      } ^
      p ^
      """ accumulation methods
      
      	These methods are used to determine the size of a group based on it's 
      	children. The initial call is like acc(totalHeight = 0, nodeHeight = 100).
      	The default implementation simply returns the the largest of the two. 
      	When a Layout is present however, that should be used to determine the outcome.
      """ ^
      p ^
      { widthAccumulation(new TestGroup)(1, 2) must_== math.max(1, 2) } ^
      { widthAccumulation(new TestGroup with TestLayout)(1, 2) must_== 4 } ^
      { heightAccumulation(new TestGroup)(1, 2) must_== math.max(1, 2) } ^
      { heightAccumulation(new TestGroup with TestLayout)(1, 2) must_== 5 } ^
      { sizeAccumulation(new TestGroup)((1, 2), (3, 4)) must_== (math.max(1, 3), math.max(2, 4)) } ^
      { sizeAccumulation(new TestGroup with TestLayout)((2, 1), (1, 2)) must_== (4, 5) } ^
      p ^
      """ determine size command gathering
      
      	These commands are called with nodes as arguments, they however react only to 
      	groups. On top of that, they only react to groups that are not dependent on 
      	their parents for size. In short they resize groups to their children.
      """ ^
      p ^
      { determineSizeCommands(new TestNode) must be empty } ^
      { determineSizeCommands(new TestGroup) must be empty } ^
      { determineSizeCommands(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineSizeCommands(new TestGroup with PercentageBasedSize { children(new TestNode) }) must be empty } ^
      { determineWidthCommands(new TestNode) must be empty } ^
      { determineWidthCommands(new TestGroup) must be empty } ^
      { determineWidthCommands(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineWidthCommands(new TestGroup with PercentageBasedHeight { children(new TestNode) }) must not be empty } ^
      { determineWidthCommands(new TestGroup with PercentageBasedWidth { children(new TestNode) }) must be empty } ^
      { determineHeightCommands(new TestNode) must be empty } ^
      { determineHeightCommands(new TestGroup) must be empty } ^
      { determineHeightCommands(new TestGroup { children(new TestNode) }) must not be empty } ^
      { determineHeightCommands(new TestGroup with PercentageBasedWidth { children(new TestNode) }) must not be empty } ^
      { determineHeightCommands(new TestGroup with PercentageBasedHeight { children(new TestNode) }) must be empty } ^
      p ^
      """ Commands
      
      	These are the commands that will eventually be executed
      """ ^
      p ^
      { //ResizeBothCommand
        val node = new TestNode with PercentageBasedSize
        ResizeBothCommand(node, new TestGroup { width = 1; height = 2; }).execute
        (node.width.value must_== 1) and (node.height.value must_== 2)
      } ^
      { //ResizeWidthCommand
        val node = new TestNode with PercentageBasedSize
        ResizeWidthCommand(node, new TestGroup { width = 1; height = 2; }).execute
        (node.width.value must_== 1) and (node.height.value must_== 0)
      } ^
      { //ResizeHeightCommand
        val node = new TestNode with PercentageBasedSize
        ResizeHeightCommand(node, new TestGroup { width = 1; height = 2; }).execute
        (node.width.value must_== 0) and (node.height.value must_== 2)
      } ^
      end

  //added RestrictedAccess so we can manually set sizes
  class TestNode extends Node with RestrictedAccess
  class TestGroup extends Group with RestrictedAccess

  trait TestLayout extends Layout { self: Group =>
    def calculateWidth(node: PercentageBasedWidth): Width = 0
    def calculateHeight(node: PercentageBasedWidth): Height = 1
    def calculateWidth(node: AnchorBasedWidth): Width = 2
    def calculateHeight(node: AnchorBasedHeight): Height = 3

    def determineTotalChildWidth(totalWidth: Double, nodeWidth: Double): Width = 4
    def determineTotalChildHeight(totalHeight: Double, nodeHeight: Double): Height = 5

    def updateLayout: Unit = {}
  }
}