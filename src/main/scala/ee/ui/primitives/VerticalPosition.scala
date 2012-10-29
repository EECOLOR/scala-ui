package ee.ui.primitives

sealed abstract class VerticalPosition

object VerticalPosition {
  
    /**
     * Indicates top vertical position.
     */
    object TOP extends VerticalPosition

    /**
     * Indicates centered vertical position.
     */
    object CENTER extends VerticalPosition

    /**
     * Indicates baseline vertical position.
     */
    object BASELINE extends VerticalPosition

    /**
     * Indicates bottom vertical position.
     */
    object BOTTOM extends VerticalPosition
}