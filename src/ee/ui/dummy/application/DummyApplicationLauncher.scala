package ee.ui.dummy.application

import ee.ui.application.ApplicationDependencies
import ee.ui.application.ApplicationLauncher
import ee.ui.dummy.nativeElements.DummyNativeManager

trait DummyApplicationLauncher extends ApplicationLauncher {
  val applicationDependencies = new ApplicationDependencies {

    val launcher = DummyLauncher
    val application = createApplication _
    val nativeManager = DummyNativeManager
  }
}