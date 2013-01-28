package ee.ui.application

import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.display.Scene
import ee.ui.display.Node
import ee.ui.display.Group
import ee.ui.layout.Layout
import ee.ui.display.Window
import ee.ui.layout.LayoutEngine

class PulseHandler(application:Application)(implicit displayImplementationHandler: DisplayImplementationHandler, layoutEngine:LayoutEngine) {

  def pulse = application.windows foreach notify _

  def notify(window: Window): Unit = {
    
    window.scene.value foreach notify _
    
    displayImplementationHandler updateImplementationOf window
  }

  def notify(scene: Scene): Unit = {
    
    layoutEngine layout scene
    
    scene.root.value foreach notify _
    
    displayImplementationHandler updateImplementationOf scene
  }

  def notify(node: Node): Unit = {
    
    node match {
      case group: Group => {
        group.children foreach notify _
      }
      case _ => //we only need to recurse for groups
    }
    
    displayImplementationHandler updateImplementationOf node
  }
}
