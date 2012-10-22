import ee.ui.application.Application
import ee.ui.dummy.application.DummyApplicationLauncher
import ee.ui.nativeElements.Stage

class TestApplication extends Application {
    def start(stage:Stage) = {
        
    }
}

object TestApplication extends DummyApplicationLauncher {
	def createApplication = new TestApplication
}