package ee.ui.application

trait ApplicationLauncher extends ImplicitApplicationDependencies {
    
    def createApplication:Application
    def applicationDependencies:ApplicationDependencies
    
    ApplicationDependencies set applicationDependencies
    
	def main(args:Array[String]) = {
	    Application launch args
	}
}