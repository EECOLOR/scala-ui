package ee.ui.application

import ee.ui.implementation.EmptyWindowImplementationHandler
import ee.ui.implementation.EngineImplementationContract
import ee.ui.implementation.WindowImplementationHandler
import ee.ui.implementation.ExitHandler
import ee.ui.implementation.EmptyExitHandler
import ee.ui.implementation.EmptyEngineImplementationContract

class StubApplicationLauncher extends ApplicationLauncher {
  type Engine = StubEngine

  trait StubEngine extends EmptyEngineImplementationContract with EngineImplementationContract

  val createdApplication = new StubApplication with Engine
  def createApplication = createdApplication

  def launch(createApplication: () => Application with Engine, args: Array[String]): Unit = ???
}