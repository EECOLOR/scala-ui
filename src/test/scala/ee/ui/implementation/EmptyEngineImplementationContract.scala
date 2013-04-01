package ee.ui.implementation

import ee.ui.application.ApplicationSettings
import ee.ui.members.ReadOnlyEvent
import ee.ui.application.Application
import scala.concurrent.promise

trait EmptyEngineImplementationContract extends EngineImplementationContract {
  override val windowImplementationHandler:WindowImplementationHandler = EmptyWindowImplementationHandler
  override val exitHandler: ExitHandler = EmptyExitHandler
  override val settings: ApplicationSettings = new ApplicationSettings(promise[Application].future)
}