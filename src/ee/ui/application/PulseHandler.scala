package ee.ui.application

import ee.ui.nativeElements.Stage
import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Scene
import ee.ui.Node
import ee.ui.Group
import ee.ui.layout.Layout
import ee.ui.nativeElements.Window

class PulseHandler(application:Application) extends ImplicitNativeManager {

  def nativeManager(implicit nativeManager: NativeManager) = nativeManager

  def pulse = application.windows foreach notify _

  def notify(window: Window): Unit = {
    println("PulseHandler.notify window")
    
    window.scene foreach notify _
    
    nativeManager updateImplementationOf window
  }

  def notify(scene: Scene): Unit = {
    scene.root foreach notify _
    
    nativeManager updateImplementationOf scene
  }

  def notify(node: Node): Unit = {
    
    node match {
      case group: Group => {
        group.children foreach notify _
      }
      case _ => //we only need to recurse for groups
    }
    
    nativeManager updateImplementationOf node
  }
}
