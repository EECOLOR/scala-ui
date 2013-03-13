package ee.ui.application.details

import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.display.Scene
import ee.ui.display.Node
import ee.ui.display.Group
import ee.ui.display.Window
import ee.ui.layout.LayoutEngine
import ee.ui.application.Application

class DefaultPulseHandler(application:Application, displayImplementationHandler: DisplayImplementationHandler, layoutEngine:LayoutEngine) extends PulseHandler {

  override def pulse() = application.windows foreach notify _

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
