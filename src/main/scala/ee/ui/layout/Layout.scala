package ee.ui.layout

import ee.ui.Group
import ee.ui.Stylable
import ee.ui.traits.LayoutSize
import ee.ui.Node
import ee.ui.properties.Add
import ee.ui.properties.Remove
import ee.ui.properties.Clear
import scala.collection.mutable.ListBuffer

trait Layout extends ChildWidthCalculator with ChildHeightCalculator with Stylable { self: Group =>
    
  def updateLayout: Unit

}

