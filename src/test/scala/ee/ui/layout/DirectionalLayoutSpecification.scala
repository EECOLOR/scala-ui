package ee.ui.layout

import org.specs2.Specification
import ee.ui.display.traits.ExplicitSize
import ee.ui.display.Group
import ee.ui.display.Node

object DirectionalLayoutSpecification extends Specification with LayoutTestHelpers {
  val engine = DefaultLayoutEngine

  def is = "Directional layout specification".title ^
    //hide ^ end
    show ^ end

  def hide = "Specification is hidden" ^ end

  def show =
    """ 
    	Directional layouts adjust the position of children so that they appear 
    	next to each other in a specific direction.
    	
    	| ? ? % ? ? | # # ? # # | __ ____ _ |
    
    	To calculate the space of percentage based children we first add the space 
    	of non parent related children to the minimal size of the anchor based 
    	nodes. 
    
    	| # # # | __ ____ _ |
    
    	The remaining space is allocated to the percentage based children.
    
    	| ? ? ? % ? ? ? | ? # ? | __ ____ _ |
    
    	If the total percentage of the percentage based children is less than a 100, 
    	that allocated space is adjusted accordingly. That gives us also the final 
    	anchor based space
    
    	| ? ? ? % ? ? | ? # ? ? | __ ____ _ |
    
    	The size factor of percentage based children is determined using the total 
    	percentages (70% of a total of 130% is not actualy 70% of the available 
    	space).
    
    	| %% %%%% %%% | ? # ? ? | __ ____ _ |
    	
    	The anchor space is devided evenly among the children. The size of the actual 
    	node is influence by the anchors (left and right for example).
    
    	| %% %%%% %%% | ##    # | __ ____ _ |
    
    	During the layout phase they are positioned in the order in which they appear in 
    	the children list.
    
    	| %%%%  ##    %% ____ %%% _   #  __ |
    
      """ ^
      br ^
      { //calculateAvailableSpaces, same as minimal space

        val testLayout = new Group with TestLayout

        val ownSize = 100
        val information = testLayout.ChildInformation(
          calculatedChildSizes = 60,
          totalChildPercentages = 50,
          minimalAnchorChildSizes = 20)

        val (availableAnchorBasedSpace, occupiedPercentageBasedSpace) =
          testLayout.calculateAvailableSpaces(ownSize, information): (Double, Double)

        (availableAnchorBasedSpace === 30) and (occupiedPercentageBasedSpace === 10)
      } ^
      { //calculateAvailableSpaces, take minSize into account

        val testLayout = new Group with TestLayout

        val ownSize = 100
        val information = testLayout.ChildInformation(
          calculatedChildSizes = 50,
          totalChildPercentages = 50,
          minimalPercentageChildSizes = 20,
          minimalAnchorChildSizes = 30)

        val (availableAnchorBasedSpace, occupiedPercentageBasedSpace) =
          testLayout.calculateAvailableSpaces(ownSize, information): (Double, Double)

        (availableAnchorBasedSpace === 30) and (occupiedPercentageBasedSpace === 20)
      } ^
      p ^
      " Horizontal layout " ^
      { //Simple layout
        val scene = new ee.ui.display.traits.Size with ExplicitSize { width = 800; height = 600 }

        val group = new TestGroupS with HorizontalLayout {
          val name = "group"
          children(
            new TestNodeP with ExplicitSize {
              val name = "node1"
              width = 10
              height = 30
              expectingPosition(0, 0)
            },
            new TestNodeP with ExplicitSize {
              val name = "node2"
              width = 20
              height = 60
              expectingPosition(10, 0)
            })
          expectingSize(30, 60)
        }

        engine.layoutWithParent(scene)(group)

        checkResults(group)
      } ^
      { //Complex layout, minWidths
        val scene = new ee.ui.display.traits.Size with ExplicitSize { width = 800; height = 600 }

        val group = new TestGroupS with HorizontalLayout {
          val name = "group"
          children(
            new TestNode with PercentageBasedSize {
              val name = "node1"
              percentWidth = 20
              percentHeight = 100
              minWidth = 10

              expecting(0, 0)(minWidth, 60)
            },
            new TestNodeP with ExplicitSize {
              val name = "node2"
              width = 20
              height = 30
              expectingPosition(10, 0)
            },
            new TestNode with AnchorBasedSize {
              val name = "node3"
              left = 10
              top = 20
              bottom = 30
              right = 40
              minWidth = 30

              expecting(40, 20)(minWidth, 10)
            },
            new TestNodeP with ExplicitSize {
              val name = "node4"
              width = 40
              height = 60
              expectingPosition(110, 0)
            })
          expectingSize(150, 60)
        }

        engine.layoutWithParent(scene)(group)

        checkResults(group)
      } ^ end

  trait TestLayout extends DirectionalLayout { self: Group =>
    def childrenResized(): Unit = ???

    def calculateChildWidth(node: Node with ParentRelatedWidth): Width = ???
    def calculateChildHeight(node: Node with ParentRelatedHeight): Height = ???

    def determineTotalChildWidth(getChildWidth: Node => Width): SizeInformationType = ???
    def determineTotalChildHeight(getChildWidth: Node => Width): SizeInformationType = ???

    def updateLayout: Unit = ???
  }
}
