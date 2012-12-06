package ee.ui.application

import ee.ui.primitives.Point
import ee.ui.nativeElements.Text

trait TextHelper {
	def getCaretPosition(text:Text, index:Int):Point
	def getCaretIndex(text:Text, position:Point):Int
}