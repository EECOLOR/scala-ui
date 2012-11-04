package ee.ui.layout

import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.Node
import ee.ui.traits.LayoutSize
import ee.ui.traits.RestrictedAccess

class DefaultLayoutEngine extends LayoutEngine {

  val resizeEngine = DefaultResizeEngine

  def layout(stage: Stage): Unit = {
    stage.scene foreach layout
  }

  def layout(scene: Scene): Unit = {
    scene.root foreach layoutWithParent(scene)
  }

  def layoutWithParent(parent: LayoutSize)(node: Node): Unit = {

    //before we try to do any layout we need to resize the node (and possibly it's children)
    resizeEngine.adjustSizeWithParent(parent, node)

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