package ee.ui.application

import ee.ui.implementation.EmptyWindowImplementationHandler
import ee.ui.implementation.EngineImplementationContract
import ee.ui.implementation.WindowImplementationHandler

class StubApplicationLauncher extends ApplicationLauncher {
  type Engine = StubEngine
  
  trait StubEngine extends EngineImplementationContract {
     val windowImplementationHandler:WindowImplementationHandler = EmptyWindowImplementationHandler
  }
  
  val application = new StubApplication with Engine
  def createApplication = application
  
  def launch(createApplication:() => Application with Engine, args:Array[String]):Unit = ???
}