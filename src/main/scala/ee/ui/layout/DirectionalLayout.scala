package ee.ui.layout

import ee.ui.display.Group
import ee.ui.display.Node
import ee.ui.members.details.Value.valueOfValue

trait DirectionalLayout extends Layout { self: Group =>
  type SizeInformationType = ChildInformation

  case class ChildInformation(
    calculatedMinimalSize: Double = 0,
    calculatedChildSizes: Double = 0,
    totalChildPercentages: Int = 0,
    minimalPercentageChildSizes: Double = 0,
    minimalAnchorChildSizes: Double = 0,
    anchorChildCount: Int = 0) extends SizeInformation

  def calculateAvailableSpaces(ownSize: Double, information: ChildInformation): (Double, Double) = {
    val ChildInformation(
      _,
      calculatedChildSizes,
      totalChildPercentages,
      minimalPercentageChildSizes,
      minimalAnchorChildSizes,
      _) = information

    // the rest can be used for the nodes with parent related size
    val rest = ownSize - calculatedChildSizes

    // we first need to determine the amount of space occupied by the percentage 
    // based nodes
    val availablePercentageBasedSpace = rest - minimalAnchorChildSizes
    // ignore everything above a 100%
    val normalizedPercentages = totalChildPercentages min 100

    // if it's below a 100, determine the factor
    val factor = normalizedPercentages / 100d
    // use the factor against the available percentage based space
    val requestedPercentageBasedSpace = availablePercentageBasedSpace * factor
    // determine the occupied space
    val occupiedPercentageBasedSpace = requestedPercentageBasedSpace max minimalPercentageChildSizes
    // now we know what space is occupied by the anchor based nodes
    val availableAnchorBasedSpace = rest - occupiedPercentageBasedSpace

    (availableAnchorBasedSpace, occupiedPercentageBasedSpace)
  }

}

trait HorizontalLayout extends DirectionalLayout { self: Group =>

  override def calculateChildWidth(node: Node with ParentRelatedWidth, groupWidth: Width, sizeInformation: SizeInformationType): Width = {

    val information = sizeInformation

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

  override def calculateChildHeight(node: Node with ParentRelatedHeight, groupHeight: Height, sizeInformation: SizeInformationType): Height =
    node calculateHeight groupHeight

  override def determineTotalChildWidth(getChildWidth: Node => Width): SizeInformationType =
    gatherChildInformation(getChildWidth)

  override def determineTotalChildHeight(getChildHeight: Node => Height): SizeInformationType =
    ChildInformation(calculatedMinimalSize = (ChildHeightCalculator.determineTotalChildHeight(this, getChildHeight)))

  def gatherChildInformation(getChildWidth: Node => Width): ChildInformation =
    (children foldLeft ChildInformation()) {
      case (c @ ChildInformation(minWidths, widths, percentages, percentageWidths, anchorWidths, anchorCount), node) => {

        val childWidth = getChildWidth(node)
        val cw = c copy (calculatedMinimalSize = minWidths + childWidth)

        node match {
          case p: PercentageBasedWidth =>
            cw copy (
              totalChildPercentages = percentages + p.percentWidth,
              minimalPercentageChildSizes = percentageWidths + childWidth)

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

    def anchorPosX(x: X, a: Node with AnchorBasedWidth): X = {
      val left: Double = a.left
      updateX(a, x + left)
      x + left + a.bounds.width + a.right
    }
    def anchorPosY(a: Node with AnchorBasedHeight) = {
      updateY(a, a.top)
    }
    def otherPosX(x: X, other: Node): X = {
      updateX(other, x)
      x + other.bounds.width
    }

    // start with x == 0
    (children foldLeft 0d) { (x, child) =>
      child match {
        case a: AnchorBasedSize => {
          anchorPosY(a)
          anchorPosX(x, a)
        }
        case a: AnchorBasedWidth => {
          updateY(a, 0)
          anchorPosX(x, a)
        }
        case a: AnchorBasedHeight => {
          anchorPosY(a)
          otherPosX(x, a)
        }

        //Note that percentage based nodes can now be handled in a similar fashion
        case other => {
          updateY(other, 0)
          otherPosX(x, other)
        }

      }
    }
  }
}

trait VerticalLayout extends DirectionalLayout { self: Group =>

  override def calculateChildHeight(node: Node with ParentRelatedHeight, groupHeight: Height, sizeInformation: SizeInformationType): Height = {

    val information = sizeInformation

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

  override def calculateChildWidth(node: Node with ParentRelatedWidth, groupWidth: Width, sizeInformation: SizeInformationType): Width =
    node calculateWidth groupWidth

  override def determineTotalChildHeight(getChildHeight: Node => Height): SizeInformationType =
    gatherChildInformation(getChildHeight)

  override def determineTotalChildWidth(getChildWidth: Node => Width): SizeInformationType =
    ChildInformation(calculatedMinimalSize = ChildWidthCalculator.determineTotalChildWidth(this, getChildWidth))

  def gatherChildInformation(getChildHeight: Node => Height): ChildInformation =
    (children foldLeft ChildInformation()) {
      case (c @ ChildInformation(minHeights, heights, percentages, percentageHeights, anchorHeights, anchorCount), node) =>

        val childHeight = getChildHeight(node)
        val ch = c copy (calculatedMinimalSize = minHeights + childHeight)

        node match {
          case p: PercentageBasedHeight =>
            ch copy (
              totalChildPercentages = percentages + p.percentHeight,
              minimalPercentageChildSizes = percentageHeights + childHeight)

          case a: AnchorBasedHeight =>
            ch copy (
              minimalAnchorChildSizes = anchorHeights + childHeight,
              anchorChildCount = anchorCount + 1)

          case unknown =>
            ch copy (calculatedChildSizes = heights + childHeight)
        }
    }

  override def updateLayout: Unit = {

    def anchorPosY(y: Y, a: Node with AnchorBasedHeight): Y = {
      val top: Double = a.top
      updateY(a, y + top)
      y + top + a.bounds.height + a.bottom
    }
    def anchorPosX(a: Node with AnchorBasedWidth) = {
      updateX(a, a.left)
    }
    def otherPosY(y: Y, other: Node): Y = {
      updateY(other, y)
      y + other.bounds.height
    }

    // start with y == 0
    (children foldLeft 0d) { (y, child) =>
      child match {
        case a: AnchorBasedSize => {
          anchorPosX(a)
          anchorPosY(y, a)
        }
        case a: AnchorBasedWidth => {
          anchorPosX(a)
          otherPosY(y, a)
        }
        case a: AnchorBasedHeight => {
          updateX(a, 0)
          anchorPosY(y, a)
        }
        //Note that percentage based nodes can now be handled in a similar fashion
        case other => {
          updateX(other, 0)
          otherPosY(y, other)
        }

      }
    }
  }
}