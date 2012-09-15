package ee.ui.application

import ee.ui.nativeElements.Stage

trait Application {
	def start(stage:Stage)
	def stop:Unit = {}
	def init:Unit = {}
}

object Application extends ImplicitApplicationDependencies {
    
    def launch(args:Array[String])(implicit launcher:Launcher):Unit = launcher launch args
}