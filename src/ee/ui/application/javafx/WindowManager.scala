package ee.ui.application.javafx

import ee.ui.impl.NativeManager

trait WindowManager extends NativeManager[ee.ui.application.Window, Window] {
	def createInstance(value:ee.ui.application.Window) = new Window(value)
}

object WindowManager extends WindowManager