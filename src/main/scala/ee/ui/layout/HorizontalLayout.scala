package ee.ui.layout

import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.Add
import ee.ui.properties.Remove
import ee.ui.properties.Clear
import ee.ui.traits.RestrictedAccess

trait HorizontalLayout extends Layout { self: Group =>

  case class ChildInformation(
    knownChildWidths: Double,
    totalChildPercentages: Int,
    minimalAnchorChildWidths: Double,
    anchorChildCount: Int)

  var childInformation: ChildInformation = _

  def childrenResized(): Unit = {
    val knownChildWidths = (nodes foldLeft 0d)((acc, node) => acc + node.width)

    val (totalChildPercentages, minimalAnchorChildWidths, anchorChildCount) =
      (nodesWithParentRelatedSize foldLeft ((0, 0d, 0))) {
        case ((totalChildPercentages, minimalAnchorChildWidths, anchorChildCount), node) =>
          node match {
            case p: PercentageBasedWidth =>
              (totalChildPercentages + p.percentWidth, minimalAnchorChildWidths, anchorChildCount)
            case a: AnchorBasedWidth =>
              (totalChildPercentages, minimalAnchorChildWidths + a.minRequiredWidth, anchorChildCount + 1)
            case unknown => (totalChildPercentages, minimalAnchorChildWidths, anchorChildCount)
          }
      }

    childInformation =
      ChildInformation(
        knownChildWidths,
        totalChildPercentages,
        minimalAnchorChildWidths,
        anchorChildCount)
  }

  def calculateChildWidth(node: Node with ParentRelatedWidth): Width = {
    val ownWidth = width

    val ChildInformation(
      knownChildWidths,
      totalChildPercentages,
      minimalAnchorChildWidths,
      anchorChildCount) = childInformation

    // the restWidth can be used for the nodes with parent related size
    val restWidth = ownWidth - knownChildWidths

    // we first need to determine the amount of space occupied by the percentage 
    // based nodes
    val availablePercentageBasedWidth = restWidth - minimalAnchorChildWidths
    // ignore everything above a 100%
    val normalizedPercentages = math min (totalChildPercentages, 100)

    // if it's below a 100, determine the factor
    val factor = normalizedPercentages / 100d
    // determine the occupied space
    val occupiedPercentageBasedWidth = availablePercentageBasedWidth * factor
    // now we know what space is occupied by the anchor based nodes
    val availableAnchorBasedWidth = restWidth - occupiedPercentageBasedWidth

    node match {
      case p: PercentageBasedWidth => {
        // if the percentage of all percentage based nodes is not 100, the percentage of 
        // this node has a different meaning, we need to normalize it
        val normalizedFactor = p.percentWidth / totalChildPercentages
        width * normalizedFactor
      }
      case a: AnchorBasedWidth => {
        // we devide the anchor based space evenly and use that to determine the size
        if (anchorChildCount < 1) throw new Error("This is weird, according to my records, this layout does not have any anchor based children")
        (availableAnchorBasedWidth / anchorChildCount) - a.left - a.right
      }
      // we don't know, use the default
      case unknown => unknown calculateWidth this
    }
  }

  def calculateChildHeight(node: Node with ParentRelatedHeight): Height =
    node calculateHeight this

  def determineTotalChildWidth(getChildWidth: Node => Width): Width = 
    (children foldLeft 0d) { (total, node) => total + getChildWidth(node) }
    
  def determineTotalChildHeight(getChildHeight: Node => Height): Height =
    Layout.determineTotalChildHeight(this, getChildHeight)

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
        //Not that percentage based nodes can now be handled in a similar fashion
        case other => {
          other.x = x
          x + other.width
        }

      }
    }
  }
}