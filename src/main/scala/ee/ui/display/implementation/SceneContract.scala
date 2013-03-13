package ee.ui.display.implementation

import ee.ui.display.Scene
import ee.ui.display.traits.SizeProxy
import ee.ui.display.traits.PositionProxy
import ee.ui.events.EventProxy
import ee.ui.events.CharacterTypedEvent
import ee.ui.events.KeyEvent
import ee.ui.events.MouseEvent
import ee.ui.system.RestrictedAccess

case class SceneContract(scene: Scene) {
  val read = scene

  object write extends PositionProxy with SizeProxy with RestrictedAccess {
    protected val target = scene
    
    val onMouseMoved = new EventProxy[MouseEvent](scene.onMouseMoved)
    val onMouseDown = new EventProxy[MouseEvent](scene.onMouseDown)
    val onMouseUp = new EventProxy[MouseEvent](scene.onMouseUp)
    
    val onKeyDown = new EventProxy[KeyEvent](scene.onKeyDown)
    val onKeyUp = new EventProxy[KeyEvent](scene.onKeyUp)
    val onCharacterTyped = new EventProxy[CharacterTypedEvent](scene.onCharacterTyped)
  }
}