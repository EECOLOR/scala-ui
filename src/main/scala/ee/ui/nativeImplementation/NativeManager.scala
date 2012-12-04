package ee.ui.nativeImplementation

import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.nativeElements.Text
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Rectangle

trait NativeManager {
  def updateImplementationOf(o: AnyRef): Unit = o match {
    case window: Window => update(window)
    case scene: Scene => update(scene)
    case group: Group => update(group)
    case text: Text => update(text)
    case rectangle: Rectangle => update(rectangle)
  }

  def windowCreated(o: Window) = register(o)

  protected def register(o: Window): Unit

  protected def update(o: Window): Unit
  protected def update(o: Scene): Unit
  protected def update(o: Group): Unit
  protected def update(o: Text): Unit
  protected def update(o: Rectangle): Unit
}
