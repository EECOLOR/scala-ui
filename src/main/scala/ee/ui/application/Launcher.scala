package ee.ui.application

import ee.ui.events.Event

trait Launcher extends ImplicitApplicationConstructor {
	def launch(args:Array[String]):Unit
	val launchComplete:Event[Application]
}