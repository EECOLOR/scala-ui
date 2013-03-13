package ee.ui.application

import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.application.details.ApplicationDependencies

trait ApplicationLauncher {

  implicit def createApplication(): Application with Windows
  implicit def applicationDependencies: ApplicationDependencies
  
  trait Windows {
    implicit def windowImplementationHandler:WindowImplementationHandler = applicationDependencies.implementationContract.displayImplementationHandler
  }
  
  def main(args: Array[String]) = {
    Application launch args
  }
}