package ee.ui.application

import ee.ui.display.implementation.EngineImplementationContract
import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.display.implementation.EmptyWindowImplementationHandler

class StubApplicationLauncher extends ApplicationLauncher {
  type Engine = StubEngine
  
  trait StubEngine extends EngineImplementationContract {
     val windowImplementationHandler:WindowImplementationHandler = EmptyWindowImplementationHandler
  }
  
  val application = new StubApplication with Engine
  def createApplication = application
  
  def launch(createApplication:() => Application with Engine, args:Array[String]):Unit = ???
}