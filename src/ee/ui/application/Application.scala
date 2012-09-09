package ee.ui.application

trait Application {
	def start(stage:Stage)
	def stop:Unit = {}
	def init:Unit = {}
}

object Application extends ImplicitDependencies {
    
    def launch(args:Array[String])(implicit launcher:Launcher):Unit = launcher launch args
}