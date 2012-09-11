package ee.ui.application.javafx

import ee.ui.application.Dependencies
import ee.ui.application.javafx
import ee.ui.impl.NativeManager

trait JavaFxDependencies extends Dependencies with JavaFxManagers {
    
	def launcher:ee.ui.application.Launcher = javafx.Launcher 
}