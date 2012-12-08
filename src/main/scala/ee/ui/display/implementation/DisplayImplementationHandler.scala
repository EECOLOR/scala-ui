package ee.ui.display.implementation

import SceneContract.apply
import WindowContract.apply
import ee.ui.display.Group
import ee.ui.display.Scene
import ee.ui.display.Text
import ee.ui.display.Window
import ee.ui.display.shape.Rectangle

trait WindowImplementationHandler {
  def windowCreated(o: Window) = register(new WindowContract(o))

  protected def register(o: WindowContract): Unit
}

trait DisplayImplementationHandler extends WindowImplementationHandler {
  def updateImplementationOf(o: AnyRef): Unit = o match {
    case window: Window => update(WindowContract(window))
    case scene: Scene => update(SceneContract(scene))
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
