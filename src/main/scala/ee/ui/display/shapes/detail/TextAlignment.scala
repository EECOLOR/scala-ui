package ee.ui.display.shapes.detail

sealed trait TextAlignment

object TextAlignment {

  object LEFT extends TextAlignment
  object CENTER extends TextAlignment
  object RIGHT extends TextAlignment
  object JUSTIFY extends TextAlignment

}