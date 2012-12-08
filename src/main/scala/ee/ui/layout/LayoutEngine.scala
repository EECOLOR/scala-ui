package ee.ui.layout

import ee.ui.display.Scene

trait LayoutEngine {
	def layout(scene:Scene):Unit
}