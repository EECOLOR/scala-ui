package ee.ui.dummy.application

import ee.ui.application.Launcher
import ee.ui.application.ImplicitApplicationDependencies
import ee.ui.application.Application
import ee.ui.nativeElements.Stage
import ee.ui.events.Event
import ee.ui.nativeElements.Stage

object DummyLauncher extends Launcher with ImplicitApplicationDependencies {
  def launch(args: Array[String]): Unit = launchApplication

  val launchComplete = new Event[Application]

  def launchApplication(implicit a: () => Application) = {
    val app = a()
    app start new Stage(true)
    launchComplete fire app
  }
}