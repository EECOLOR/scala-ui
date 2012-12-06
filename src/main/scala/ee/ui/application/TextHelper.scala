package ee.ui.application

import ee.ui.primitives.Point
import ee.ui.nativeElements.Text
import ee.ui.primitives.Font
import ee.ui.primitives.FontMetrics

trait TextHelper {
	def getCaretPosition(text:Text, index:Int):Point
	def getCaretIndex(text:Text, position:Point):Int
	def getFontMetrics(font:Font):FontMetrics
}