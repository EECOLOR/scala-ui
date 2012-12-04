package ee.ui.layout

import ee.ui.Group
import ee.ui.Node
import ee.ui.properties.Property
import ee.ui.traits.RestrictedAccess
import ee.ui.traits.AccessRestriction
import ee.ui.primitives.Bounds
import ee.ui.primitives.Identity
import ee.ui.primitives.Transformation
import ee.ui.traits.Rotation
import ee.ui.traits.Scaling
import ee.ui.traits.RuntimeError
import ee.ui.primitives.Point

trait PartialParentRelatedSize extends NoTransformations

/**
 * This trait helps prevent the use of rotation and scaling which does 
 * not make a lot of sense for parent related size
 * 
 * In theory we could allow transformations, parent related size elements 
 * would then scale if it's not a pure translation. I wonder if there ever 
 * comes a compelling use case
 */
trait NoTransformations extends Scaling with Rotation {

  def scaleX_=(value: Double)(implicit ev: RuntimeError) = super.scaleX_=(value)
  def scaleY_=(value: Double)(implicit ev: RuntimeError) = super.scaleY_=(value)
  def scaleZ_=(value: Double)(implicit ev: RuntimeError) = super.scaleZ_=(value)

  def rotation_=(value: Double)(implicit ev: RuntimeError) = super.rotation_=(value)
  def rotationAxis_=(value: Point)(implicit ev: RuntimeError) = super.rotationAxis_=(value)

}

object PartialParentRelatedSize {
  val transformationAppliedError =
    new Error(
      "Parent related size can not be calculated for nodes that have transformations " +
        "applied. Note that things like rotation and scaling are also performed with " +
        "transformations.")

  def ifIsPureTranslation(transformation: Transformation)(body: => Unit) =
    if (transformation.isPureTranslation) {
      body
    } else {
      throw transformationAppliedError
    }
}

trait ParentRelatedWidth extends PartialParentRelatedSize with ee.ui.traits.Width { self: Node =>

  def minRequiredWidth: Width = minWidth

  def calculateWidth(parentWidth: Width): Width

  private val _minWidth = new Property(0d)
  def minWidth = _minWidth
  def minWidth_=(value: Double) = _minWidth.value = value

  override def width_=(value: Double)(implicit ev: AccessRestriction) =
    (PartialParentRelatedSize ifIsPureTranslation totalTransformation) {

      super.width_=(math.max(value, minWidth))
    }

}

trait ParentRelatedHeight extends PartialParentRelatedSize with ee.ui.traits.Height { self: Node =>

  def minRequiredHeight: Height = minHeight

  def calculateHeight(parentHeight: Height): Height

  private val _minHeight = new Property(0d)
  def minHeight = _minHeight
  def minHeight_=(value: Double) = _minHeight.value = value

  override def height_=(value: Double)(implicit ev: AccessRestriction) =
    (PartialParentRelatedSize ifIsPureTranslation totalTransformation) {

      super.height_=(math.max(value, minHeight))
    }

}

trait PercentageBasedWidth extends ParentRelatedWidth { self: Node =>

  override def calculateWidth(parentWidth: Width): Width =
    (percentWidth / 100d) * parentWidth

  private val _percentWidth = new Property(100)
  def percentWidth = _percentWidth
  def percentWidth_=(value: Int) = _percentWidth.value = value
}

trait PercentageBasedHeight extends ParentRelatedHeight { self: Node =>

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