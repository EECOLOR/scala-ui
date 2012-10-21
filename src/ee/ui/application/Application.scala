package ee.ui.application

import ee.ui.nativeElements.Stage
import ee.ui.events.PulseEvent

trait Application {
	def init:Unit = {}
	def start(stage:Stage)
	def stop:Unit = {}
}

object Application extends ImplicitApplicationDependencies {
    
    def launch(args:Array[String])(implicit launcher:Launcher):Unit = {
    	launcher launchComplete createPulseHandler _
    	launcher launch args
    }
    
    def createPulseHandler(application:Application)(implicit pulseEvent:PulseEvent) = {
      val pulseHandler = new PulseHandler
      pulseEvent(pulseHandler.pulse)
    }
}