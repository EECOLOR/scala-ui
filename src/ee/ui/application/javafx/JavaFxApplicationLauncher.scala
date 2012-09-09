package ee.ui.application.javafx

import ee.ui.application.ImplicitDependencies
import ee.ui.application.Application

trait JavaFxApplicationLauncher extends ImplicitDependencies {
    ImplicitDependencies set new JavaFxDependencies {
        def application = createApplication
    }
    
    def createApplication:Application
    
	def main(args:Array[String]) = {
	    Application launch args
	}
}