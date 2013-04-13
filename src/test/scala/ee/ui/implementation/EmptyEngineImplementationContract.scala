package ee.ui.implementation

import ee.ui.application.ApplicationSettings

trait EmptyEngineImplementationContract extends EngineImplementationContract {

  override val windowImplementationHandler: WindowImplementationHandler =
    EmptyWindowImplementationHandler

  override val exitHandler: ExitHandler =
    EmptyExitHandler

  override val settings: ApplicationSettings =
    new ApplicationSettings
}