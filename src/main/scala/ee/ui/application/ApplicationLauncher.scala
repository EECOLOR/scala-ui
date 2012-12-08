package ee.ui.application

import ee.ui.display.implementation.WindowImplementationHandler

trait ApplicationLauncher  {

    implicit def createApplication():Application
    def applicationDependencies:ApplicationDependencies
    
    implicit def pulseEvent = applicationDependencies.implementationContract.pulseEvent
    implicit def launcher = applicationDependencies.implementationContract.launcher
    implicit def displayImplementationHandler = applicationDependencies.implementationContract.elementImplementationHandler
    implicit def layoutEngine = applicationDependencies.layoutEngine
    implicit def windowImplementationHandler:WindowImplementationHandler = displayImplementationHandler 
    
	def main(args:Array[String]) = {
	    Application launch args
	}
}