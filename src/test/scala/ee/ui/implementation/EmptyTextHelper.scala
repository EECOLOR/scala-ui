package ee.ui.implementation

import ee.ui.display.shapes.Text
import ee.ui.primitives.Point

object EmptyTextHelper extends TextHelper {
  def getCaretPosition(text: Text, index: Int): Point = Point.ZERO
}