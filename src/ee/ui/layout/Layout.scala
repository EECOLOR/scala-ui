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
  
  def totalChildSize(node:Node, width:Double, height:Double, nodeWidth:Double, nodeHeight:Double):Size

  //def updateLayout:Unit
}