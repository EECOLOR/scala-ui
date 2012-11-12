package ee.ui.events

case class MouseEvent(
    button:MouseButton, 
    clickCount:Int,
    sceneX:Double,
    sceneY:Double,
    screenX:Double,
    screenY:Double,
    altDown:Boolean,
    controlDown:Boolean,
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