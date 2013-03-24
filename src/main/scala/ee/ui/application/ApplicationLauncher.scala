package ee.ui.application

import ee.ui.display.Window
import ee.ui.members.ReadOnlyEvent
import ee.ui.system.RestrictedAccess

abstract class ApplicationLauncher {
  val applicationCreated = ReadOnlyEvent[Application]()
  
  def createApplication():Application
  
  def launch(createApplication:() => Application, args:Array[String]):Unit
  
  def internalCreateApplication():Application = {
    val application = createApplication()
    ReadOnlyEvent.fire(applicationCreated, application)(RestrictedAccess)
    application
  }
  def main(args:Array[String]):Unit = launch(internalCreateApplication, args)
}

