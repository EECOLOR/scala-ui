package ee.ui.application.javafx

import ee.ui.application.Dependencies
import ee.ui.application.javafx

abstract class JavaFxDependencies extends Dependencies {
	def stage:Boolean => ee.ui.application.Stage = {primary:Boolean => new javafx.Stage(primary)}
	def launcher:ee.ui.application.Launcher = javafx.Launcher 
}