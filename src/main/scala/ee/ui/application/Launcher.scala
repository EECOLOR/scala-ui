package ee.ui.application

import ee.ui.events.Event

trait Launcher {
	def launch(args:Array[String])(implicit createApplication: () => Application):Unit
	val launchComplete:Event[Application]
}