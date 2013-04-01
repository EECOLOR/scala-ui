package ee.ui.application

import ee.ui.members.ReadOnlyEvent
import ee.ui.system.RestrictedAccess
import ee.ui.implementation.EngineImplementationContract
import scala.concurrent.promise

abstract class ApplicationLauncher {
  private val applicationPromise = promise[Application with Engine]
  val application = applicationPromise.future
  
  def createApplication():Application with Engine
  
  def launch(createApplication:() => Application with Engine, args:Array[String]):Unit
  
  def internalCreateApplication():Application with Engine = {
    val application = createApplication()
    applicationPromise success application
    application
  }
  
  def main(args:Array[String]):Unit = launch(internalCreateApplication, args)
  
  type Engine <: EngineImplementationContract 
}

