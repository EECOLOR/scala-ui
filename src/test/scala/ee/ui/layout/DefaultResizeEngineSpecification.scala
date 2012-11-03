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

object DefaultResizeEngineSpecification extends Specification with LayoutTestHelpers {

  val engine = new DefaultResizeEngine

  def is = "DefaultResizeEngine specification".title ^
    hide ^ end
    //show ^ end

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
          override def calculateWidth(parentWidth: Width): Width = parentWidth / 2
        }
        trait ParentRelatedHeight extends ee.ui.layout.ParentRelatedHeight { self: Node =>
          override def calculateHeight(parentHeight: Height): Height = parentHeight / 2
        }

        trait ParentRelatedSize extends ParentRelatedWidth with ParentRelatedHeight { self: Node =>
        }

        val scene = new LayoutSize with ExplicitSize { width = 200; height = 100 }

        val root = new TestGroupS with ParentRelatedSize {
          val name = "root"
          expectingSize(100, 50)
          children(
            new TestGroupS with ParentRelatedSize {
              val name = "group0"
              expectingSize(50, 25)
            },
            new TestGroupS with ParentRelatedWidth {
              val name = "group1"
              expectingSize(50, 0)
            },
            new TestGroupS with ParentRelatedHeight {
              val name = "group2"
              expectingSize(0, 25)
            },
            new TestGroupS with ExplicitSize {
              val name = "group7"
              width = 70
              height = 20
              expectingSize(70, 20)
            },
            new TestGroupS with ExplicitWidth {
              val name = "group8"
              width = 70
              expectingSize(70, 0)
            },
            new TestGroupS with ExplicitHeight {
              val name = "group9"
              height = 20
              expectingSize(0, 20)
            },
            new TestGroupS {
              val name = "group10"
              expectingSize(0, 0)
            },
            new TestGroupS with TestLayout {
              val name = "group11"
              expectingSize(0, 0)
            },
            new TestGroupS {
              val name = "groep12"

              expectingSize(60, 40)

              children(
                new TestNodeS {
                  val name = "node1"
                  expectingSize(0, 0)
                },
                new TestNodeS with ParentRelatedSize {
                  val name = "node2"
                  expectingSize(30, 20)
                },
                new TestNodeS with ParentRelatedWidth {
                  val name = "node3"
                  expectingSize(30, 0)
                },
                new TestNodeS with ParentRelatedHeight {
                  val name = "node4"
                  expectingSize(0, 20)
                },
                new TestNodeS with ExplicitSize {
                  val name = "node5"
                  width = 50
                  height = 30
                  expectingSize(50, 30)
                },
                new TestNodeS with ExplicitWidth {
                  val name = "node6"
                  width = 60
                  expectingSize(60, 0)
                },
                new TestNodeS with ExplicitHeight {
                  val name = "node7"
                  height = 40
                  expectingSize(0, 40)
                })

            },
            new TestGroupS with TestLayout {
              val name = "groep13"
              expectingSize(140, 84)
              children(
                new TestNodeS {
                  val name = "node1"
                  expectingSize(0, 0)
                },
                new TestNodeS with ParentRelatedSize {
                  val name = "node2"
                  minWidth = 20
                  minHeight = 10
                  expectingSize(70, 42)
                },
                new TestNodeS with ParentRelatedWidth {
                  val name = "node3"
                  minWidth = 10
                  expectingSize(70, 0)
                },
                new TestNodeS with ParentRelatedHeight {
                  val name = "node4"
                  minHeight = 4
                  expectingSize(0, 42)
                },
                new TestNodeS with ExplicitSize {
                  val name = "node5"
                  width = 50
                  height = 30
                  expectingSize(50, 30)
                },
                new TestNodeS with ExplicitWidth {
                  val name = "node6"
                  width = 60
                  expectingSize(60, 0)
                },
                new TestNodeS with ExplicitHeight {
                  val name = "node7"
                  height = 40
                  expectingSize(0, 40)
                })

            })

        }

        engine.adjustSizeWithParent(scene, root)

        checkResults(root)
      } ^
      end

  trait TestLayout extends Layout { self: Group =>
    def childrenResized(): Unit = {}

    def calculateChildWidth(node: Node with ParentRelatedWidth): Width = node calculateWidth node.width
    def calculateChildHeight(node: Node with ParentRelatedHeight): Height = node calculateHeight node.height

    def determineTotalChildWidth(getChildWidth: Node => Width): Width =
      (children foldLeft 0d) { (total, node) => total + getChildWidth(node) }

    def determineTotalChildHeight(getChildHeight: Node => Height): Height =
      (children foldLeft 0d) { (total, node) => total + getChildHeight(node) }

    def updateLayout: Unit = {}
  }

}