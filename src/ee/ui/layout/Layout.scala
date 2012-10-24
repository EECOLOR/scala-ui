package ee.ui.layout

import ee.ui.Group
import ee.ui.Stylable
import ee.ui.traits.LayoutSize

trait Layout extends Stylable { self: Group =>

  def determineSize(node: PercentageBased): (Double, Double)
  def determineSize(node: AnchorBased): (Double, Double)

  //def updateLayout:Unit
}