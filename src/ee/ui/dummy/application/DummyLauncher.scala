package ee.ui.dummy.application

import ee.ui.application.Launcher
import ee.ui.application.ImplicitApplicationDependencies
import ee.ui.application.Application
import ee.ui.nativeElements.Stage

object DummyLauncher extends Launcher with ImplicitApplicationDependencies {
  def launch(args: Array[String]): Unit = launchApplication
  
  def launchApplication(implicit a: () => Application) = a().start(new Stage(true))
}