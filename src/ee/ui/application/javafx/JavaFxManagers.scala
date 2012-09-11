package ee.ui.application.javafx

import ee.ui.application.Dependencies

trait JavaFxManagers {
	implicit def windowManager = WindowManager
	implicit def stageManager = StageManager
}