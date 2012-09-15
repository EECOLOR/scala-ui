package ee.ui.application

trait Launcher extends ImplicitApplicationDependencies {
	def launch(args:Array[String]):Unit
}