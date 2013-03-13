package ee.ui.display.implementation

import SceneContract.apply
import WindowContract.apply
import ee.ui.display.Group
import ee.ui.display.Scene
import ee.ui.display.Text
import ee.ui.display.Window
import ee.ui.display.shape.Rectangle
import ee.ui.display.Node

trait WindowImplementationHandler {
  def show(o: Window): Unit = show(WindowContract(o))
  def hide(o: Window): Unit = hide(WindowContract(o))

  protected def show(o: WindowContract): Unit
  protected def hide(o: WindowContract): Unit
}

trait DisplayImplementationHandler extends WindowImplementationHandler {
  
  def updateImplementationOf(window: Window): Unit = update(WindowContract(window))
  def updateImplementationOf(scene: Scene): Unit = update(SceneContract(scene))

  def updateImplementationOf(node: Node): Unit =
    node match {
      case group: Group => update(group)
      case text: Text => update(text)
      case rectangle: Rectangle => update(rectangle)
    }

  protected def update(o: WindowContract): Unit
  protected def update(o: SceneContract): Unit
  
  protected def update(o: Group): Unit
  protected def update(o: Text): Unit
  protected def update(o: Rectangle): Unit
}
