package ee.ui.application

import ee.ui.events.Event
import ee.ui.nativeElements.Stage

trait Launcher extends ImplicitApplicationDependencies {
	def launch(args:Array[String]):Unit
	val launchComplete:Event[Application]
}