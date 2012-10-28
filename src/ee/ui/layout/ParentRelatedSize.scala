package ee.ui.layout

import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.Property
import ee.ui.traits.RestrictedAccess
import ee.ui.traits.LayoutSize
import ee.ui.traits.LayoutWidth
import ee.ui.traits.LayoutHeight

trait PartialParentRelatedSize

sealed trait ParentRelatedWidth extends PartialParentRelatedSize { self: Node =>
  def calculateWidth(parent: LayoutWidth): Width

  def adjustWidthTo(parent: LayoutWidth): Unit = {
    val newWidth =
      parent match {
        case layout: Layout =>
          this match {
            case node: PercentageBasedSize => layout calculateWidth node
            case node: AnchorBasedSize => layout calculateWidth node
          }
        case parent => calculateWidth(parent)
      }

    implicit val access = RestrictedAccess

    width = newWidth
  }

  private val _minWidth = new Property(0d)
  def minWidth = _minWidth
  def minWidth_=(value: Double) = _minWidth.value = value
}

sealed trait ParentRelatedHeight extends PartialParentRelatedSize { self: Node =>

  def calculateHeight(parent: LayoutHeight): Height

  def adjustHeightTo(parent: LayoutHeight): Unit = {
    val newHeight =
      parent match {
        case layout: Layout =>
          this match {
            case node: PercentageBasedSize => layout calculateHeight node
            case node: AnchorBasedSize => layout calculateHeight node
          }
        case parent => calculateHeight(parent)
      }

    implicit val access = RestrictedAccess

    height = newHeight
  }

  private val _minHeight = new Property(0d)
  def minHeight = _minHeight
  def minHeight_=(value: Double) = _minHeight.value = value
}

trait PercentageBasedWidth extends ParentRelatedWidth { self: Node =>

  def calculateWidth(parent: LayoutSize): Width =
    (percentWidth / 100) * parent.width

  private val _percentWidth = new Property(100)
  def percentWidth = _percentWidth
  def percentWidth_=(value: Int) = _percentWidth.value = value
}

trait PercentageBasedHeight extends ParentRelatedHeight { self: Node =>

  def calculateHeight(parent: LayoutSize): Height =
    (percentHeight / 100) * parent.height

  private val _percentHeight = new Property(100)
  def percentHeight = _percentHeight
  def percentHeight_=(value: Int) = _percentHeight.value = value
}

trait PercentageBasedSize extends PercentageBasedWidth with PercentageBasedHeight { self: Node => }

trait AnchorBasedWidth extends ParentRelatedWidth { self: Node =>

  def calculateWidth(parent: LayoutSize): Width =
    parent.width - left - right

  private val _left = new Property(0d)
  def left = _left
  def left_=(value: Double) = _left.value = value

  private val _right = new Property(0d)
  def right = _right
  def right_=(value: Double) = _right.value = value
}

trait AnchorBasedHeight extends ParentRelatedWidth { self: Node =>

  def calculateHeight(parent: LayoutSize): Height =
    parent.height - top - bottom

  private val _top = new Property(0d)
  def top = _top
  def top_=(value: Double) = _top.value = value

  private val _bottom = new Property(0d)
  def bottom = _bottom
  def bottom_=(value: Double) = _bottom.value = value
}

trait AnchorBasedSize extends AnchorBasedWidth with AnchorBasedHeight { self: Node => }