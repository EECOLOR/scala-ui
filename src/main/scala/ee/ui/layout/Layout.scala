package ee.ui.layout

import ee.ui.Group
import ee.ui.Stylable
import ee.ui.traits.LayoutSize
import ee.ui.Node

trait Layout extends Stylable { self: Group =>

  def calculateChildWidth(node:Node with ParentRelatedWidth):Width
  def calculateChildHeight(node:Node with ParentRelatedHeight):Height
  
  def determineTotalChildWidth(totalWidth:Double, nodeWidth:Double):Width
  def determineTotalChildHeight(totalHeight:Double, nodeHeight:Double):Height
  
  def updateLayout:Unit
}