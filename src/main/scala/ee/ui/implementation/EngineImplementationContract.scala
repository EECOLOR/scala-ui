package ee.ui.implementation

import ee.ui.application.ApplicationSettings

trait EngineImplementationContract {
  val windowImplementationHandler: WindowImplementationHandler
  val exitHandler: ExitHandler
  val settings: ApplicationSettings
}