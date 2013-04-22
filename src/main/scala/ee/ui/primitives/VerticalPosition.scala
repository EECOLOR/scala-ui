package ee.ui.primitives

sealed trait VerticalPosition

object VerticalPosition {
  
    object TOP extends VerticalPosition
    object CENTER extends VerticalPosition
    object BOTTOM extends VerticalPosition
}