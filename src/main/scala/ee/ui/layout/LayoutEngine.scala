package ee.ui.layout

import ee.ui.nativeElements.Scene

trait LayoutEngine {
	def layout(scene:Scene):Unit
}