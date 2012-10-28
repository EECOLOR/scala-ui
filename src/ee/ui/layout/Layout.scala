package ee.ui.layout

import ee.ui.Group
import ee.ui.Stylable
import ee.ui.traits.LayoutSize
import ee.ui.Node

trait Layout extends Stylable { self: Group =>

  def calculateWidth(node:PercentageBasedWidth):Width
  def calculateHeight(node:PercentageBasedWidth):Height
  def calculateWidth(node:AnchorBasedWidth):Width
  def calculateHeight(node:AnchorBasedHeight):Height
  
  def updateTotalChildWidth(totalWidth:Double, nodeWidth:Double):Width
  def updateTotalChildHeight(totalHeight:Double, nodeHeight:Double):Height
  
  //def updateLayout:Unit
}