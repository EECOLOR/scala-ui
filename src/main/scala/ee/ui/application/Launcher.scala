package ee.ui.application

import ee.ui.members.Event

trait Launcher {
	def launch(args:Array[String])(implicit createApplication: () => Application):Unit
	val launchComplete:Event[Application]
}