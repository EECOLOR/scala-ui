package ee.ui.application.details

import ee.ui.application.Application
import ee.ui.members.ReadOnlyEvent

trait Launcher {
	def launch(args:Array[String])(implicit createApplication: () => Application):Unit
	val launchComplete:ReadOnlyEvent[Application]
}