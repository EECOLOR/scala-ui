package ee.ui.layout

import org.specs2.Specification
import ee.ui.traits.ExplicitSize
import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.ReadOnlyProperty

object DefaultResizeEngineSpecification extends Specification {

  def is =
    "DefaultResizeEngine specification".title ^
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
    		- Layout
    	
    	Note that the parent related sizes can be mixed onto Node and Group. Layout can 
    	only be mixed onto Group.
    
    	If a Group's size is not parent related, it will be resized to it's children. If 
    	the Group has a Layout, the layout is used to determine it's size. Node sizes are 
    	not changed if they are not parent related.
    
    	The `adjustSize` method in the examples uses a root node with width 200 and height 100
      """ ^
      br ^
      { // Do not change size of the node
        val node = new Node with ExplicitSize { width = 10; height = 20 }

        adjustSize(node)

        (10d ==== node.width) and (20d ==== node.height)
      } ^
      { // Change size of the node
        val node = new Node with PercentageBasedSize { percentWidth = 100; percentHeight = 100 }

        adjustSize(node)

        (200d ==== node.width) and (100d ==== node.height)
      } ^
      { // Change size of the group to the parent
        val node = new Group with PercentageBasedSize {
          percentWidth = 100; percentHeight = 100
          children(new Node with ExplicitSize { width = 10; height = 20 })
        }

        adjustSize(node)

        (200d ==== node.width) and (100d ==== node.height)
      } ^
      { // Change size of the group to the child
        val node = new Group {
          children(new Node with ExplicitSize { width = 10; height = 20 })
        }

        adjustSize(node)

        (10d ==== node.width) and (20d ==== node.height)
      } ^
      //TODO make this one example that captures all aspects
      end

  trait TestLayout extends Layout { self: Group =>
    def calculateWidth(node: PercentageBasedWidth): Width = node calculateWidth this
    def calculateHeight(node: PercentageBasedHeight): Height = node calculateHeight this
    def calculateWidth(node: AnchorBasedWidth): Width = ???
    def calculateHeight(node: AnchorBasedHeight): Height = ???

    def determineTotalChildWidth(totalWidth: Double, nodeWidth: Double): Width = 
      totalWidth + nodeWidth
      
    def determineTotalChildHeight(totalHeight: Double, nodeHeight: Double): Height = 
      totalHeight + nodeHeight

    def updateLayout: Unit = {}
  }

  def adjustSize(node: Node): Unit = {
    val engine = new DefaultResizeEngine

    val rootNode = new Group with ExplicitSize {
      width = 200
      height = 100

    }
    engine.adjustSizeWithParent(rootNode, node)
  }

}