package ee.ui.layout

import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.Add
import ee.ui.properties.Remove
import ee.ui.properties.Clear
import ee.ui.traits.RestrictedAccess

trait DirectionalLayout extends Layout { self: Group =>

  case class ChildInformation(
    calculatedChildSizes: Double = 0,
    totalChildPercentages: Int = 0,
    minimalAnchorChildSizes: Double = 0,
    anchorChildCount: Int = 0)

  private var _childInformation: ChildInformation = _

  def childInformation = _childInformation
  def childInformation_=(value: ChildInformation) = _childInformation = value

  def calculateAvailableAnchorSpace(ownSize: Double, information: ChildInformation): Double = {
    val ChildInformation(
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

    availableAnchorBasedSpace
  }

}

trait HorizontalLayout extends DirectionalLayout { self: Group =>

  def calculateChildWidth(node: Node with ParentRelatedWidth): Width = {

    val information = childInformation

    node match {
      case p: PercentageBasedWidth => {
        // if the percentage of all percentage based nodes is not 100, the percentage of 
        // this node has a different meaning, we need to normalize it
        val normalizedFactor = p.percentWidth / information.totalChildPercentages
        width * normalizedFactor
      }
      case a: AnchorBasedWidth => {
        val anchorChildCount = information.anchorChildCount
        // we devide the anchor based space evenly and use that to determine the size
        if (anchorChildCount < 1) throw new Error("This is weird, according to my records, this layout does not have any anchor based children")
        (calculateAvailableAnchorSpace(width, information) / anchorChildCount) - a.left - a.right
      }
      // we don't know, use the default
      case unknown => unknown calculateWidth this
    }
  }

  def calculateChildHeight(node: Node with ParentRelatedHeight): Height =
    node calculateHeight this

  def determineTotalChildWidth(getChildWidth: Node => Width): Width = {

    val newChildInformation = gatherChildInformation(getChildWidth)

    //save the child information
    childInformation = newChildInformation

    newChildInformation.calculatedChildSizes
  }

  def determineTotalChildHeight(getChildHeight: Node => Height): Height =
    Layout.determineTotalChildHeight(this, getChildHeight)

  def gatherChildInformation(getChildWidth: Node => Width): ChildInformation =
    (children foldLeft ChildInformation()) {
      case (c @ ChildInformation(widths, percentages, anchorWidths, anchorCount), node) =>
        node match {
          case p: PercentageBasedWidth =>
            c copy (totalChildPercentages = percentages + p.percentWidth)

          case a: AnchorBasedWidth =>
            c copy (
              minimalAnchorChildSizes = anchorWidths + a.minRequiredWidth,
              anchorChildCount = anchorCount + 1)

          case unknown =>
            c copy (calculatedChildSizes = widths + getChildWidth(unknown))
        }
    }

  def updateLayout: Unit = {
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

  def calculateChildHeight(node: Node with ParentRelatedHeight): Height = {

    val information = childInformation

    node match {
      case p: PercentageBasedHeight => {
        // if the percentage of all percentage based nodes is not 100, the percentage of 
        // this node has a different meaning, we need to normalize it
        val normalizedFactor = p.percentHeight / information.totalChildPercentages
        height * normalizedFactor
      }
      case a: AnchorBasedHeight => {
        val anchorChildCount = information.anchorChildCount
        // we devide the anchor based space evenly and use that to determine the size
        if (anchorChildCount < 1) throw new Error("This is weird, according to my records, this layout does not have any anchor based children")
        (calculateAvailableAnchorSpace(height, information) / anchorChildCount) - a.top - a.bottom
      }
      // we don't know, use the default
      case unknown => unknown calculateHeight this
    }
  }

  def calculateChildWidth(node: Node with ParentRelatedWidth): Width =
    node calculateWidth this

  def determineTotalChildHeight(getChildHeight: Node => Height): Height = {

    val newChildInformation = gatherChildInformation(getChildHeight)

    //save the child information
    childInformation = newChildInformation

    newChildInformation.calculatedChildSizes
  }

  def determineTotalChildWidth(getChildWidth: Node => Width): Width =
    Layout.determineTotalChildWidth(this, getChildWidth)

  def gatherChildInformation(getChildHeight: Node => Height): ChildInformation =
    (children foldLeft ChildInformation()) {
      case (c @ ChildInformation(heights, percentages, anchorHeights, anchorCount), node) =>
        node match {
          case p: PercentageBasedHeight =>
            c copy (totalChildPercentages = percentages + p.percentHeight)

          case a: AnchorBasedHeight =>
            c copy (
              minimalAnchorChildSizes = anchorHeights + a.minRequiredHeight,
              anchorChildCount = anchorCount + 1)

          case unknown =>
            c copy (calculatedChildSizes = heights + getChildHeight(unknown))
        }
    }

  def updateLayout: Unit = {
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