package ee.ui.application

import ee.ui.nativeElements.Stage
import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Scene
import ee.ui.Node
import ee.ui.Group
import ee.ui.Layout
import ee.ui.nativeElements.Window

class PulseHandler(application:Application) extends ImplicitNativeManager {

  def nativeManager(implicit nativeManager: NativeManager) = nativeManager

  def pulse = application.windows foreach notify _

  def notify(window: Window): Unit = {
    println("PulseHandler.notify window")
    
    nativeManager updateImplementationOf window

    window.scene foreach notify _
  }

  def notify(scene: Scene): Unit = {
    nativeManager updateImplementationOf scene

    scene.root foreach { group =>
      layout(group)
      notify(group)
    }
  }

  def layout(group:Group):Unit = {
    group.children foreach { 
      case group:Group => layout(group)
      case _ => //no need to do layout for a non group
    }
    
    group match {
      case layout:Layout => {
        layout.updateLayout
      }
      case _ => //group does not have a layout
    }
  }
  
  def notify(node: Node): Unit = {
    nativeManager updateImplementationOf node
    
    node match {
      case group: Group => {
        group.children foreach notify _
      }
      case _ => //we only need to recurse for groups
    }
  }
}
