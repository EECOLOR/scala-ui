import ee.ui.application.Application
import ee.ui.application.ImplicitDependencies
import ee.ui.application.javafx.JavaFxDependencies
import ee.ui.application.Stage
import ee.ui.application.javafx.JavaFxApplicationLauncher

class TestApplication extends Application {
    def start(stage:Stage) = {
        
    }
}

object TestApplication extends JavaFxApplicationLauncher {
	def createApplication = new TestApplication
}