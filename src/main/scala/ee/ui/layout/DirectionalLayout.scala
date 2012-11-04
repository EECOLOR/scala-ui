package ee.ui.layout

import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.Add
import ee.ui.properties.Remove
import ee.ui.properties.Clear
import ee.ui.traits.RestrictedAccess

trait DirectionalLayout extends Layout { self: Group =>
  type SizeInformationType = TestInternalSizeInformation
  
  class TestInternalSizeInformation(val size: Double) extends SizeInformation  
  
  case class ChildInformation(
    calculatedMinimalSize: Double = 0,
    calculatedChildSizes: Double = 0,
    totalChildPercentages: Int = 0,
    minimalAnchorChildSizes: Double = 0,
    anchorChildCount: Int = 0)

  private var _childInformation: ChildInformation = _

  def childInformation = _childInformation
  def childInformation_=(value: ChildInformation) = _childInformation = value

  def calculateAvailableSpaces(ownSize: Double, information: ChildInformation): (Double, Double) = {
    val ChildInformation(
      _,
      calculatedChildSizes,
      totalChildPercentages,
      minimalAnchorChildSizes,
      _) = information

    // the rest can be used for the nodes with parent related size
    val rest = ownSize - calculatedChildSizes

    // we first need to determine the amount of space occupied by the percentage 
    // based nodes
    val availablePercentageBasedSpace = rest - minimalAnchorChildSizes
    // ignore everything above a 100%
    val normalizedPercentages = math min (totalChildPercentages, 100)

    // if it's below a 100, determine the factor
    val factor = normalizedPercentages / 100d
    // determine the occupied space
    val occupiedPercentageBasedSpace = availablePercentageBasedSpace * factor
    // now we know what space is occupied by the anchor based nodes
    val availableAnchorBasedSpace = rest - occupiedPercentageBasedSpace

    (availableAnchorBasedSpace, occupiedPercentageBasedSpace)
  }

}

trait HorizontalLayout extends DirectionalLayout { self: Group =>

  override def calculateChildWidth(node: Node with ParentRelatedWidth, groupWidth:Width, sizeInformation: SizeInformationType): Width = {

    val information = childInformation

    val ownWidth: Width = groupWidth

    val (availableAnchorBasedSpace, occupiedPercentageBasedSpace) =
      calculateAvailableSpaces(ownWidth, information)

    node match {
      case p: PercentageBasedWidth => {
        // if the percentage of all percentage based nodes is not 100, the percentage of 
        // this node has a different meaning, we need to normalize it
        val normalizedFactor = p.percentWidth / information.totalChildPercentages
        occupiedPercentageBasedSpace * normalizedFactor
      }
      case a: AnchorBasedWidth => {
        val anchorChildCount = information.anchorChildCount
        // we devide the anchor based space evenly and use that to determine the size
        if (anchorChildCount < 1) throw new Error("This is weird, according to my records, this layout does not have any anchor based children")
        (availableAnchorBasedSpace / anchorChildCount) - a.left - a.right
      }
      // we don't know, use the default
      case unknown => unknown calculateWidth ownWidth
    }
  }

  override def calculateChildHeight(node: Node with ParentRelatedHeight, groupHeight:Height, sizeInformation: SizeInformationType): Height =
    node calculateHeight groupHeight

  override def determineTotalChildWidth(getChildWidth: Node => Width): SizeInformationType = {

    val newChildInformation = gatherChildInformation(getChildWidth)

    //save the child information
    childInformation = newChildInformation

    new TestInternalSizeInformation(newChildInformation.calculatedMinimalSize)
  }

  override def determineTotalChildHeight(getChildHeight: Node => Height): SizeInformationType =
    new TestInternalSizeInformation(ChildHeightCalculator.determineTotalChildHeight(this, getChildHeight))

  def gatherChildInformation(getChildWidth: Node => Width): ChildInformation =
    (children foldLeft ChildInformation()) {
      case (c @ ChildInformation(minWidths, widths, percentages, anchorWidths, anchorCount), node) => {

        val childWidth = getChildWidth(node)
        val cw = c copy (calculatedMinimalSize = minWidths + childWidth)

        node match {
          case p: PercentageBasedWidth =>
            cw copy (totalChildPercentages = percentages + p.percentWidth)

          case a: AnchorBasedWidth =>
            cw copy (
              minimalAnchorChildSizes = anchorWidths + childWidth,
              anchorChildCount = anchorCount + 1)

          case unknown =>
            cw copy (calculatedChildSizes = widths + childWidth)
        }
      }
    }

  override def updateLayout: Unit = {
    // we need access to set x and y positions
    implicit val access = RestrictedAccess

    // start with x == 0
    (children foldLeft 0d) { (x, child) =>
      child match {
        case a: AnchorBasedWidth => {
          val left: Double = a.left
          a.x = x + left
          x + left + a.width + a.right
        }
        //Note that percentage based nodes can now be handled in a similar fashion
        case other => {
          other.x = x
          x + other.width
        }

      }
    }
  }
}

trait VerticalLayout extends DirectionalLayout { self: Group =>

  override def calculateChildHeight(node: Node with ParentRelatedHeight, groupHeight:Height, sizeInformation: SizeInformationType): Height = {

    val information = childInformation

    val ownHeight: Height = groupHeight

    val (availableAnchorBasedSpace, occupiedPercentageBasedSpace) =
      calculateAvailableSpaces(ownHeight, information)

    node match {
      case p: PercentageBasedHeight => {
        // if the percentage of all percentage based nodes is not 100, the percentage of 
        // this node has a different meaning, we need to normalize it
        val normalizedFactor = p.percentHeight / information.totalChildPercentages
        occupiedPercentageBasedSpace * normalizedFactor
      }
      case a: AnchorBasedHeight => {
        val anchorChildCount = information.anchorChildCount
        // we devide the anchor based space evenly and use that to determine the size
        if (anchorChildCount < 1) throw new Error("This is weird, according to my records, this layout does not have any anchor based children")
        (availableAnchorBasedSpace / anchorChildCount) - a.top - a.bottom
      }
      // we don't know, use the default
      case unknown => unknown calculateHeight ownHeight
    }
  }

  override def calculateChildWidth(node: Node with ParentRelatedWidth, groupWidth:Width, sizeInformation: SizeInformationType): Width =
    node calculateWidth groupWidth

  override def determineTotalChildHeight(getChildHeight: Node => Height): SizeInformationType = {

    val newChildInformation = gatherChildInformation(getChildHeight)

    //save the child information
    childInformation = newChildInformation

    new TestInternalSizeInformation(newChildInformation.calculatedMinimalSize)
  }

  override def determineTotalChildWidth(getChildWidth: Node => Width): SizeInformationType =
     new TestInternalSizeInformation(ChildWidthCalculator.determineTotalChildWidth(this, getChildWidth))

  def gatherChildInformation(getChildHeight: Node => Height): ChildInformation =
    (children foldLeft ChildInformation()) {
      case (c @ ChildInformation(minHeights, heights, percentages, anchorHeights, anchorCount), node) =>

        val childHeight = getChildHeight(node)
        val ch = c copy (calculatedMinimalSize = minHeights + childHeight)

        node match {
          case p: PercentageBasedHeight =>
            ch copy (totalChildPercentages = percentages + p.percentHeight)

          case a: AnchorBasedHeight =>
            ch copy (
              minimalAnchorChildSizes = anchorHeights + childHeight,
              anchorChildCount = anchorCount + 1)

          case unknown =>
            ch copy (calculatedChildSizes = heights + childHeight)
        }
    }

  override def updateLayout: Unit = {
    // we need access to set x and y positions
    implicit val access = RestrictedAccess

    // start with y == 0
    (children foldLeft 0d) { (y, child) =>
      child match {
        case a: AnchorBasedHeight => {
          val top: Double = a.top
          a.y = y + top
          y + top + a.height + a.bottom
        }
        //Note that percentage based nodes can now be handled in a similar fashion
        case other => {
          other.y = y
          y + other.height
        }

      }
    }
  }
}