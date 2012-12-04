package ee.ui.layout

import org.specs2.Specification
import ee.ui.Group
import ee.ui.Node
import language.postfixOps
import ee.ui.traits.RestrictedAccess
import org.specs2.specification.Fragments
import ee.ui.traits.ExplicitSize
import ee.ui.traits.ExplicitHeight
import ee.ui.traits.ExplicitWidth
/**/
object DefaultResizeEngineInternalsSpecification extends Specification with LayoutTestHelpers {

  val engine = DefaultResizeEngine

  import engine._

  def is = "DefaultResizeEngine internals Specification".title ^
    hide ^ end
    //show ^ end

  def hide = "Specification is hidden" ^ end

  def show =
    "Unit tests on methods" ^
      updateSpecs ^
      end

  val updateSpecs =
    "update" ^
      "width" ^
      br ^
      { // update.width ofChild node
        val node = new TestNodeS with TestParentRelatedWidth {
          val name = "node"
          expectingSize(1, 0)
        }
        implicit val p = new TestParentWidthInformation(childWidth = 1)

        update.width ofChild node

        checkResults(node)
      } ^
      { // update.width of group
        val group = new TestGroupS {
          val name = "group"
          expectingSize(1, 0)
        }
        implicit val p = new TestParentWidthInformation(parentWidth = 1)

        update.width of group

        checkResults(group)
      } ^
      p ^
      "height" ^
      br ^
      { // update.height ofChild node
        val node = new TestNodeS with TestParentRelatedHeight {
          val name = "node"
          expectingSize(0, 1)
        }
        implicit val p = new TestParentHeightInformation(childHeight = 1)

        update.height ofChild node

        checkResults(node)
      } ^
      { // update.height of group
        val group = new TestGroupS {
          val name = "group"
          expectingSize(0, 1)
        }
        implicit val p = new TestParentHeightInformation(parentHeight = 1)

        update.height of group

        checkResults(group)
      } ^
      end ^
      "retrieve" ^
      "sizeInformation" ^
      { // retrieve.sizeInformation of group
        val group = new Group {
          children(new Node with ExplicitSize { width = 2; height = 4 })
        }

        val sizeInformation = retrieve.sizeInformation of group

        val testNode = new Node with ParentRelatedSize {}

        (sizeInformation.width === 2) and
          (sizeInformation.height === 4) and
          (sizeInformation.childWidthFunction(testNode) === 1) and
          (sizeInformation.childHeightFunction(testNode) === 2)
      } ^
      { // retrieve.sizeInformation of group - with explicit size
        val group = new Group with ExplicitSize {
          width = 8
          height = 16
          children(new Node with ExplicitSize { width = 2; height = 4 })
        }

        val sizeInformation = retrieve.sizeInformation of group

        val testNode = new Node with ParentRelatedSize {}

        (sizeInformation.width === 8) and
          (sizeInformation.height === 16) and
          (sizeInformation.childWidthFunction(testNode) === 4) and
          (sizeInformation.childHeightFunction(testNode) === 8)
      } ^
      { // retrieve.sizeInformation of group - with parent related size
        val group = new Group with ParentRelatedSize {
          //we need to fake that the size has been set already
          implicit val access = RestrictedAccess
          width = 8
          height = 16
          children(new Node with ExplicitSize { width = 2; height = 4 })
        }

        val sizeInformation = retrieve.sizeInformation of group

        val testNode = new Node with ParentRelatedSize {}

        (sizeInformation.width === 8) and
          (sizeInformation.height === 16) and
          (sizeInformation.childWidthFunction(testNode) === 4) and
          (sizeInformation.childHeightFunction(testNode) === 8)
      } ^
      { // retrieve.sizeInformation of layoutSize
        val layoutSize = new ee.ui.traits.Size with ExplicitSize { width = 2; height = 4 }

        val sizeInformation = retrieve.sizeInformation of layoutSize

        val testNode = new Node with ParentRelatedSize {}

        (sizeInformation.width === 2) and
          (sizeInformation.height === 4) and
          (sizeInformation.childWidthFunction(testNode) === 1) and
          (sizeInformation.childHeightFunction(testNode) === 2)
      } ^
      end ^
      "adjust" ^
      "size" ^
      br ^
      { // adjust.size of group - one deep
        val group = new TestGroupS {
          val name = "group"
          expectingSize(1, 2)
          children(
            new TestNodeS with ExplicitSize {
              val name = "node1"
              expectingSize(1, 2)
              width = 1
              height = 2
            })
        }

        val actions = adjust.size of group
        actions.force

        checkResults(group)
      } ^
      { // adjust.size of group - two deep
        val group = new TestGroupS {
          val name = "group"
          expectingSize(1, 2)
          children(
            new TestGroupS {
              val name = "innerGroup"
              expectingSize(1, 2)
              children(
                new TestNodeS with ExplicitSize {
                  val name = "node1"
                  expectingSize(1, 2)
                  width = 1
                  height = 2
                })
            })
        }

        val actions = adjust.size of group
        actions.force

        checkResults(group)
      } ^
      { // adjust.size of group - two deep - parent related
        val group = new TestGroupS with ExplicitSize {
          val name = "group"
          width = 10
          height = 30
          expectingSize(10, 30)
          children(
            new TestGroupS with ParentRelatedSize {
              val name = "innerGroup"
              expectingSize(5, 15)
              children(
                new TestNodeS with ExplicitSize {
                  val name = "node1"
                  expectingSize(1, 2)
                  width = 1
                  height = 2
                })
            })
        }

        val actions = adjust.size of group
        actions.force

        checkResults(group)
      } ^
      { // adjust.size ofChild group - two deep - parent related

        val group = new TestGroupS with ParentRelatedSize {
          val name = "group"
          expectingSize(10, 30)
          children(
            new TestGroupS with ParentRelatedSize {
              val name = "innerGroup"
              expectingSize(5, 15)
              children(
                new TestNodeS with ExplicitSize {
                  val name = "node1"
                  expectingSize(1, 2)
                  width = 1
                  height = 2
                })
            })
        }

        implicit val sizeInformation = retrieve.sizeInformation of new ee.ui.traits.Size with ExplicitSize {
          width = 20
          height = 60
        }

        val actions = adjust.size ofChild group
        actions.force

        checkResults(group)
      } ^ end

  class TestParentWidthInformation(
    val parentWidth: Double = 0,
    val childWidth: Double = 0) extends ParentWidthInformation {

    override val width = parentWidth
    override val childWidthFunction = { n: Node with ParentRelatedWidth => childWidth }
  }

  class TestParentHeightInformation(
    val parentHeight: Double = 0,
    val childHeight: Double = 0) extends ParentHeightInformation {

    override val height = parentHeight
    override val childHeightFunction = { n: Node with ParentRelatedHeight => childHeight }
  }

  class TestParentSizeInformation(
    val parentWidth: Double = 0,
    val parentHeight: Double = 0,
    val childWidth: Double = 0,
    val childHeight: Double = 0) extends ParentSizeInformation {

    override val width = parentWidth
    override val childWidthFunction = { n: Node with ParentRelatedWidth => childWidth }
    override val height = parentHeight
    override val childHeightFunction = { n: Node with ParentRelatedHeight => childHeight }
  }

  trait TestParentRelatedWidth extends ParentRelatedWidth { self: Node =>
    override def calculateWidth(parentWidth: Width): Width = parentWidth / 2
  }
  trait TestParentRelatedHeight extends ParentRelatedHeight { self: Node =>
    override def calculateHeight(parentHeight: Height): Height = parentHeight / 2
  }

  trait ParentRelatedSize extends TestParentRelatedWidth with TestParentRelatedHeight { self: Node =>
  }

  trait TestLayout extends Layout { self: Group =>
    type SizeInformationType = TestInternalSizeInformation
    case class TestInternalSizeInformation(val calculatedMinimalSize: Double) extends SizeInformation

    override def calculateChildWidth(
      node: Node with ParentRelatedWidth, groupWidth: Width, sizeInformation: SizeInformationType): Width =
      2

    override def calculateChildHeight(
      node: Node with ParentRelatedHeight, groupHeight: Height, sizeInformation: SizeInformationType): Height =
      3

    override def determineTotalChildWidth(getChildWidth: Node => Width): SizeInformationType =
      new TestInternalSizeInformation(4)

    override def determineTotalChildHeight(getChildHeight: Node => Height): SizeInformationType =
      new TestInternalSizeInformation(5)

    override def updateLayout: Unit = {}
  }
}
