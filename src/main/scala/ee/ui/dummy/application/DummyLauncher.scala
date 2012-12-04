package ee.ui.dummy.application

import ee.ui.application.Launcher
import ee.ui.application.Application
import ee.ui.events.Event
import ee.ui.nativeElements.Window

object DummyLauncher extends Launcher {
  def launch(args: Array[String])(implicit a: () => Application): Unit = launchApplication

  val launchComplete = new Event[Application]

  def launchApplication(implicit a: () => Application) = {
    val app = a()
    app start new Window(true)
    launchComplete fire app
  }
}