package ee.ui.application.javafx

import ee.ui.impl.NativeManager

trait StageManager extends NativeManager[ee.ui.application.Stage, Stage] {
	def createInstance(value:ee.ui.application.Stage) = new Stage(value)
}

object StageManager extends StageManager
