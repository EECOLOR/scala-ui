package ee.ui.application

import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.application.details.ApplicationDependencies
import ee.ui.system.Platform
import ee.ui.system.DefaultPlatform
import ee.ui.members.Event

trait ApplicationLauncher {

  def createApplication(): Application with Engine

  protected val applicationCreated = Event[Application]
  private implicit def internalCreateApplication(): Application with Engine = {
    val application = createApplication()
    applicationCreated fire application
    application
  }

  protected implicit val applicationDependencies: ApplicationDependencies

  private val ic = applicationDependencies.implementationContract

  protected val internalPlatform: Platform =
    new DefaultPlatform(ic.launcher, applicationCreated)
  
  protected val internalWindowImplementationHandler: WindowImplementationHandler =
    ic.displayImplementationHandler

  trait Engine {
    val windowImplementationHandler = internalWindowImplementationHandler
    val platform = internalPlatform
  }

  def main(args: Array[String]) = {
    Application launch args
  }
}