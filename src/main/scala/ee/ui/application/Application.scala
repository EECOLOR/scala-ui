package ee.ui.application

import ee.ui.events.PulseEvent
import ee.ui.nativeElements.Window
import scala.collection.mutable.ListBuffer

trait Application {
  
	def show(window:Window):Unit = {
	  Window show window
	}
	
	def hide(window:Window):Unit = {
	  Window hide window
	}
  
	def windows = Window.windows
	
	def init:Unit = {}
	def start(window:Window)
	def stop:Unit = {}
}

object Application extends ImplicitPulseEvent {
    
    def launch(args:Array[String])(implicit launcher:Launcher):Unit = {
    	launcher launchComplete {
    		println("launchComplete")
    	}
    	launcher launchComplete createPulseHandler _
    	launcher launch args
    }
    
    def createPulseHandler(application:Application)(implicit pulseEvent:PulseEvent) = {
      val pulseHandler = new PulseHandler(application)
      pulseEvent(pulseHandler.pulse)
      pulseEvent.fire
    }
}