package ee.ui.layout

import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.Property
import ee.ui.traits.RestrictedAccess
import ee.ui.traits.LayoutSize
import ee.ui.traits.LayoutWidth
import ee.ui.traits.LayoutHeight
import ee.ui.traits.AccessRestriction

trait PartialParentRelatedSize

trait ParentRelatedWidth extends PartialParentRelatedSize with LayoutWidth { self: Node =>

  def minRequiredWidth: Width = minWidth
  def calculateWidth(parentWidth: Width): Width

  private val _minWidth = new Property(0d)
  def minWidth = _minWidth
  def minWidth_=(value: Double) = _minWidth.value = value
  
  override def width_=(value:Double)(implicit ev: AccessRestriction) = 
    super.width_=(math.max(value, minWidth))
}

trait ParentRelatedHeight extends PartialParentRelatedSize with LayoutHeight { self: Node =>

  def minRequiredHeight: Height = minHeight
  def calculateHeight(parentHeight: Height): Height

  private val _minHeight = new Property(0d)
  def minHeight = _minHeight
  def minHeight_=(value: Double) = _minHeight.value = value
  
  override def height_=(value:Double)(implicit ev: AccessRestriction) = 
    super.height_=(math.max(value, minHeight))

}

trait PercentageBasedWidth extends ParentRelatedWidth { self: Node =>

  override def minRequiredWidth = minWidth
  override def calculateWidth(parentWidth: Width): Width =
    (percentWidth / 100d) * parentWidth

  private val _percentWidth = new Property(100)
  def percentWidth = _percentWidth
  def percentWidth_=(value: Int) = _percentWidth.value = value
}

trait PercentageBasedHeight extends ParentRelatedHeight { self: Node =>

  override def minRequiredHeight = minHeight
  override def calculateHeight(parentHeight: Height): Height =
    (percentHeight / 100d) * parentHeight

  private val _percentHeight = new Property(100)
  def percentHeight = _percentHeight
  def percentHeight_=(value: Int) = _percentHeight.value = value
}

trait PercentageBasedSize extends PercentageBasedWidth with PercentageBasedHeight { self: Node => }

trait AnchorBasedWidth extends ParentRelatedWidth { self: Node =>

  override def minRequiredWidth =
    0d + minWidth + left + right

  override def calculateWidth(parentWidth: Width): Width =
    parentWidth - left - right

  private val _left = new Property(0d)
  def left = _left
  def left_=(value: Double) = _left.value = value

  private val _right = new Property(0d)
  def right = _right
  def right_=(value: Double) = _right.value = value
}

trait AnchorBasedHeight extends ParentRelatedHeight { self: Node =>

  override def minRequiredHeight =
    0d + minHeight + top + bottom

  override def calculateHeight(parentHeight: Height): Height =
    parentHeight - top - bottom

  private val _top = new Property(0d)
  def top = _top
  def top_=(value: Double) = _top.value = value

  private val _bottom = new Property(0d)
  def bottom = _bottom
  def bottom_=(value: Double) = _bottom.value = value
}

trait AnchorBasedSize extends AnchorBasedWidth with AnchorBasedHeight { self: Node => }