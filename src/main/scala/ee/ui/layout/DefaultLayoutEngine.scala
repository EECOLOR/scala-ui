package ee.ui.layout

import ee.ui.display.Scene
import ee.ui.display.Group
import ee.ui.system.RestrictedAccess
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.Node

object DefaultLayoutEngine extends LayoutEngine {

  def layout(scene: Scene): Unit = {
    scene.root foreach layoutWithParent(scene)
  }

  def layoutWithParent(parent: ReadOnlySize)(node: Node): Unit = {

    //before we try to do any layout we need to resize the node (and possibly it's children)
    DefaultResizeEngine.adjustSizeWithParent(parent, node)

    //now we can do the layout
    layout(node)
  }

  def layout(node: Node): Unit = {
    node match {
      case group: Group => layout(group)
      case node => //no need for layout
    }
  }

  def layout(group: Group): Unit = {
    //first layout children
    group.children foreach layout
    
    group match {
      case layout:Layout => layout.updateLayout
      case group => //no need for layout
    }
  }
}