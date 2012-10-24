package ee.ui.layout

import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.Property
import ee.ui.traits.RestrictedAccess
import ee.ui.traits.LayoutSize

sealed trait ParentRelatedSize { self: Node =>
  
  def determineSize(parent: LayoutSize): Unit = {
    val (newWidth, newHeight) = parent match {
      case layout: Layout =>
        this match {
          case node: PercentageBased => layout determineSize node
          case node: AnchorBased => layout determineSize node
        }
      case parent => calculateSize(parent)
    }

    implicit val access = RestrictedAccess

    width = newWidth
    height = newHeight
  }

  def calculateSize(parent: LayoutSize): (Double, Double)
}

trait PercentageBased extends ParentRelatedSize { self: Node =>
  
  private val _percentWidth = new Property(100)
  def percentWidth = _percentWidth
  def percentWidth_=(value: Int) = _percentWidth.value = value

  private val _percentHeight = new Property(100)
  def percentHeight = _percentHeight
  def percentHeight_=(value: Int) = _percentHeight.value = value

  def calculateSize(parent: Group): (Double, Double) =
    ((percentWidth / 100) * parent.width, (percentHeight / 100) * parent.height)
}

trait AnchorBased extends ParentRelatedSize { self: Node =>
  
  private val _top = new Property(0d)
  def top = _top
  def top_=(value: Double) = _top.value = value

  private val _left = new Property(0d)
  def left = _left
  def left_=(value: Double) = _left.value = value

  private val _bottom = new Property(0d)
  def bottom = _bottom
  def bottom_=(value: Double) = _bottom.value = value

  private val _right = new Property(0d)
  def right = _right
  def right_=(value: Double) = _right.value = value

  def calculateSize(parent: Group): (Double, Double) =
    (parent.width - left - right, parent.height - top - bottom)
}