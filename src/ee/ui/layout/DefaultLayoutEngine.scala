package ee.ui.layout

import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.Node
import ee.ui.traits.LayoutSize

class DefaultLayoutEngine extends LayoutEngine {
  def layout(stage: Stage): Unit = {
    stage.scene foreach layout _
  }

  def layout(scene: Scene): Unit = {
    scene.root foreach (layout(_, scene))
  }

  def layout(node: Node, parent:LayoutSize): Unit = {

    //size the node
    node match {
	  case node:ParentRelatedSize => node determineSize parent
	  case parent:Group => {
		parent.children foreach (layout(_, node))
	    parent.resizeToChildren
	  }
	  case node => // no need to resize
    }

  }
}