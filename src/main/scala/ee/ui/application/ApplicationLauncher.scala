package ee.ui.application

import ee.ui.members.ReadOnlyEvent
import ee.ui.system.RestrictedAccess
import ee.ui.implementation.EngineImplementationContract

abstract class ApplicationLauncher {
  val applicationCreated = ReadOnlyEvent[Application with Engine]()
  
  def createApplication():Application with Engine
  
  def launch(createApplication:() => Application with Engine, args:Array[String]):Unit
  
  def internalCreateApplication():Application with Engine = {
    val application = createApplication()
    ReadOnlyEvent.fire(applicationCreated, application)(RestrictedAccess)
    application
  }
  
  def main(args:Array[String]):Unit = launch(internalCreateApplication, args)
  
  type Engine <: EngineImplementationContract 
}

