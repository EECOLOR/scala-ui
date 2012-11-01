package ee.ui.layout

import org.specs2.Specification
import ee.ui.traits.ExplicitSize
import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.ReadOnlyProperty
import ee.ui.traits.LayoutSize
import org.specs2.matcher.MatchResult
import org.specs2.execute.Result
import org.specs2.matcher.Matcher
import ee.ui.traits.ExplicitWidth
import ee.ui.traits.ExplicitHeight
import ee.ui.traits.LayoutWidth
import ee.ui.traits.LayoutHeight

object DefaultResizeEngineSpecification extends Specification {

  val engine = new DefaultResizeEngine

  def is = "DefaultResizeEngine specification".title ^
    //hide ^ end
    show ^ end

  def hide = "Specification is hidden" ^ end

  def show =
    """ Introduction
    
    	Resizing is an important part of layout. It's also quite complex. The default
    	engine first determines a set of commands and then executes all those commands. 
    	The reason for this is that I found it tough to mix the actual resizing with 
    	first gathering a set of commands, the code became messy very fast.
    	
    	The engine takes into account the following types when calculating sizes:
    		
    		- Node
    		- Group
    		- ParentRelatedSize
    		- ParentRelatedWidth
    		- ParentRelatedHeight
    		- ExplicitSize
  			- ExplicitWidth
  			- ExplicitHeight
    		- Layout
    	
    	Note that the parent related sizes can be mixed onto Node and Group. Layout can 
    	only be mixed onto Group.
    
    	If a Group's size is not parent related or explicit, it will be resized to it's 
    	children. If the Group has a Layout, the layout is used to determine it's size. 
    	Node sizes are not changed if they are not parent related.
    
    	The example below shows a structure that captures all cases.
    
    	For testing I added a simple trait implemented by TestNode and TestGroup that 
    	requires a name and allows us to expect a size.
      """ ^
      br ^
      { //
        trait ParentRelatedWidth extends ee.ui.layout.ParentRelatedWidth { self: Node =>
          def calculateWidth(parent: LayoutWidth): Width = parent.width / 2
        }
        trait ParentRelatedHeight extends ee.ui.layout.ParentRelatedHeight { self: Node =>
          def calculateHeight(parent: LayoutHeight): Height = parent.height / 2
        }

        trait ParentRelatedSize extends ParentRelatedWidth with ParentRelatedHeight { self: Node =>
        }

        val scene = new LayoutSize with ExplicitSize { width = 200; height = 100 }

        val root = new TestGroup with ParentRelatedSize {
          val name = "root"
          expectedSize(100, 50)
          children(
            new TestGroup with ParentRelatedSize {
              val name = "group0"
              expectedSize(50, 25)
            },
            new TestGroup with ParentRelatedWidth {
              val name = "group1"
              expectedSize(50, 0)
            },
            new TestGroup with ParentRelatedHeight {
              val name = "group2"
              expectedSize(0, 25)
            },
            new TestGroup with ExplicitSize {
              val name = "group7"
              width = 70
              height = 20
              expectedSize(70, 20)
            },
            new TestGroup with ExplicitWidth {
              val name = "group8"
              width = 70
              expectedSize(70, 0)
            },
            new TestGroup with ExplicitHeight {
              val name = "group9"
              height = 20
              expectedSize(0, 20)
            },
            new TestGroup {
              val name = "group10"
              expectedSize(0, 0)
            },
            new TestGroup with TestLayout {
              val name = "group11"
              expectedSize(0, 0)
            },
            new TestGroup {
              val name = "groep12"

              expectedSize(60, 40)

              children(
                new TestNode {
                  val name = "node1"
                  expectedSize(0, 0)
                },
                new TestNode with ParentRelatedSize {
                  val name = "node2"
                  expectedSize(30, 20)
                },
                new TestNode with ParentRelatedWidth {
                  val name = "node3"
                  expectedSize(30, 0)
                },
                new TestNode with ParentRelatedHeight {
                  val name = "node4"
                  expectedSize(0, 20)
                },
                new TestNode with ExplicitSize {
                  val name = "node5"
                  width = 50
                  height = 30
                  expectedSize(50, 30)
                },
                new TestNode with ExplicitWidth {
                  val name = "node6"
                  width = 60
                  expectedSize(60, 0)
                },
                new TestNode with ExplicitHeight {
                  val name = "node7"
                  height = 40
                  expectedSize(0, 40)
                })

            },
            new TestGroup with TestLayout {
              val name = "groep13"
              expectedSize(140, 84)
              children(
                new TestNode {
                  val name = "node1"
                  expectedSize(0, 0)
                },
                new TestNode with ParentRelatedSize {
                  val name = "node2"
                  minWidth = 20
                  minHeight = 10
                  expectedSize(70, 42)
                },
                new TestNode with ParentRelatedWidth {
                  val name = "node3"
                  minWidth = 10
                  expectedSize(70, 0)
                },
                new TestNode with ParentRelatedHeight {
                  val name = "node4"
                  minHeight = 4
                  expectedSize(0, 42)
                },
                new TestNode with ExplicitSize {
                  val name = "node5"
                  width = 50
                  height = 30
                  expectedSize(50, 30)
                },
                new TestNode with ExplicitWidth {
                  val name = "node6"
                  width = 60
                  expectedSize(60, 0)
                },
                new TestNode with ExplicitHeight {
                  val name = "node7"
                  height = 40
                  expectedSize(0, 40)
                })

            })

        }

        engine.adjustSizeWithParent(scene, root)

        checkResults(root)
      } ^
      end

  trait ExpectedSize { self: Node with LayoutSize =>
    private var expectedWidth: Width = Double.NaN
    private var expectedHeight: Height = Double.NaN

    val name: String

    def expectedSize(width: Width, height: Height) = {
      expectedWidth = width
      expectedHeight = height
    }

    def beExpected: Matcher[Size] = (size: Size) => {
      val (width, height) = size

      def displayName(node: Node with ExpectedSize): String =
        node.parent.map(parent => displayName(parent.asInstanceOf[Node with ExpectedSize]) + " - ").getOrElse("") + node.name

      val path = displayName(this)
      (expectedWidth == width && expectedHeight == height, s"$path failed, expected ($expectedWidth,$expectedHeight), got $size")
    }

    def sizeExpected = (width.value, height.value) must beExpected
  }

  def checkResults(node: ExpectedSize): Result = {
    node match {
      case group: TestGroup => {
        group.children.foldLeft(node.sizeExpected: Result) { (matcher, node) =>
          matcher and checkResults(node.asInstanceOf[ExpectedSize])
        }

      }
      case node => node.sizeExpected
    }
  }

  trait TestNode extends Node with ExpectedSize
  trait TestGroup extends Group with ExpectedSize

  trait TestLayout extends Layout { self: Group =>
    def childrenResized():Unit = {}
    
    def calculateChildWidth(node: Node with ParentRelatedWidth): Width = node calculateWidth this
    def calculateChildHeight(node: Node with ParentRelatedHeight): Height = node calculateHeight this

    def determineTotalChildWidth(totalWidth: Double, nodeWidth: Double): Width =
      totalWidth + nodeWidth

    def determineTotalChildHeight(totalHeight: Double, nodeHeight: Double): Height =
      totalHeight + nodeHeight

    def updateLayout: Unit = {}
  }

}