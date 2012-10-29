package ee.ui.layout

import ee.ui.nativeElements.Stage

trait LayoutEngine {
	def layout(stage:Stage):Unit
}