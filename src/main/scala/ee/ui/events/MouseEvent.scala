package ee.ui.events

//TODO order properties, make sure KeyEvent and CharacterTypedEvent have the same ordering and that the ordering is logical
case class MouseEvent(
    button:MouseButton, 
    clickCount:Int,
    sceneX:Double,
    sceneY:Double,
    screenX:Double,
    screenY:Double,
    altDown:Boolean,
    ctrlDown:Boolean,
    metaDown:Boolean,
    middleButtonDown:Boolean,
    primaryButtonDown:Boolean,
    secondaryButtonDown:Boolean,
    shiftDown:Boolean,
    shortcutDown:Boolean,
    synthesized:Boolean)

sealed abstract class MouseButton

object MouseButton {
  object MIDDLE extends MouseButton
  object NONE extends MouseButton
  object PRIMARY extends MouseButton
  object SECONDARY extends MouseButton
}