package ee.ui.nativeImplementation

import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.nativeElements.Text
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Rectangle

trait NativeManager {
  def updateImplementationOf(o: AnyRef): Unit = o match {
    case stage: Stage => update(stage)
    case scene: Scene => update(scene)
    case group: Group => update(group)
    case text: Text => update(text)
    case rectangle: Rectangle => update(rectangle)
  }

  def windowCreated(o: Window) =
    o match {
      case stage: Stage => register(stage)
    }

  protected def register(o: Stage): Unit

  protected def update(o: Stage): Unit
  protected def update(o: Scene): Unit
  protected def update(o: Group): Unit
  protected def update(o: Text): Unit
  protected def update(o: Rectangle): Unit
}
