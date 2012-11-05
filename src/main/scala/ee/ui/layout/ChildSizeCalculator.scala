package ee.ui.layout

import ee.ui.Node
import ee.ui.Group

trait PartialChildSizeCalculator {
  trait SizeInformation {
    def calculatedMinimalSize: Double
  }

  type SizeInformationType <: SizeInformation

  case class DefaultSizeInformation(calculatedMinimalSize: Double) extends SizeInformation
}

trait ChildWidthCalculator extends PartialChildSizeCalculator {

  def calculateChildWidth(node: Node with ParentRelatedWidth, parentWidth:Width, sizeInformation: SizeInformationType): Width =
    node calculateWidth parentWidth

  def determineTotalChildWidth(getChildWidth: Node => Width): SizeInformationType
}

object ChildWidthCalculator {

  def determineTotalChildWidth(group: Group, getChildWidth: Node => Width): Width =
    (group.children foldLeft 0d) { (total, node) => math max (total, getChildWidth(node)) }
}

trait ChildHeightCalculator extends PartialChildSizeCalculator {
  
  def calculateChildHeight(node: Node with ParentRelatedHeight, parentHeight:Width, sizeInformation: SizeInformationType): Height =
    node calculateHeight parentHeight

  def determineTotalChildHeight(getChildHeight: Node => Height): SizeInformationType
}

object ChildHeightCalculator {
  def determineTotalChildHeight(group: Group, getChildHeight: Node => Height): Height =
    (group.children foldLeft 0d) { (total, node) => math max (total, getChildHeight(node)) }
}

trait GroupChildWidthCalculator extends ChildWidthCalculator {
  val group: Group

  type SizeInformationType = DefaultSizeInformation

  def determineTotalChildWidth(getChildWidth: Node => Width) = {
    val totalChildWidth = ChildWidthCalculator.determineTotalChildWidth(group, getChildWidth)

    DefaultSizeInformation(totalChildWidth)
  }

}

trait GroupChildHeightCalculator extends ChildHeightCalculator {
  val group: Group

  type SizeInformationType = DefaultSizeInformation

  def determineTotalChildHeight(getChildHeight: Node => Height) = {
    val totalChildHeight = ChildHeightCalculator.determineTotalChildHeight(group, getChildHeight)

    DefaultSizeInformation(totalChildHeight)
  }
}

trait GroupChildSizeCalculator extends ChildWidthCalculator with ChildHeightCalculator


