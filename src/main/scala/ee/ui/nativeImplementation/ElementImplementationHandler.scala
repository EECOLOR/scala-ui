package ee.ui.nativeImplementation

import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.nativeElements.Text
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Rectangle
import ee.ui.traits.ExplicitPosition
import ee.ui.traits.PositionProxy
import ee.ui.traits.SizeProxy
import ee.ui.traits.FocusProxy
import ee.ui.events.EventProxy
import ee.ui.events.MouseEvent
import ee.ui.events.KeyEvent
import ee.ui.events.CharacterTypedEvent

trait ElementImplementationHandler {
  def updateImplementationOf(o: AnyRef): Unit = o match {
    case window: Window => update(WindowContract(window))
    case scene: Scene => update(SceneContract(scene))
    case group: Group => update(group)
    case text: Text => update(text)
    case rectangle: Rectangle => update(rectangle)
  }

  def windowCreated(o: Window) = register(new WindowContract(o))

  protected def register(o: WindowContract): Unit

  protected def update(o: WindowContract): Unit
  protected def update(o: SceneContract): Unit
  protected def update(o: Group): Unit
  protected def update(o: Text): Unit
  protected def update(o: Rectangle): Unit
}

case class WindowContract(window: Window) {
  val read = window

  object write extends PositionProxy with SizeProxy with FocusProxy {
    val target = window
  }
}

case class SceneContract(scene: Scene) {
  val read = scene

  object write extends PositionProxy with SizeProxy {
    val target = scene
    
    val onMouseMoved = new EventProxy[MouseEvent](scene.onMouseMoved)
    val onMouseDown = new EventProxy[MouseEvent](scene.onMouseDown)
    val onMouseUp = new EventProxy[MouseEvent](scene.onMouseUp)
    
    val onKeyDown = new EventProxy[KeyEvent](scene.onKeyDown)
    val onKeyUp = new EventProxy[KeyEvent](scene.onKeyUp)
    val onCharacterTyped = new EventProxy[CharacterTypedEvent](scene.onCharacterTyped)
  }
}
