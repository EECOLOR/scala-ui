package ee.ui.application

import scala.concurrent.promise
import ee.ui.implementation.EngineImplementationContract

abstract class ApplicationLauncher {

  private val applicationPromise = promise[Application with Engine]
  val application = applicationPromise.future
  
  def main(args: Array[String]): Unit = launch(internalCreateApplication, args)

  def internalCreateApplication(): Application with Engine = {
    val application = createApplication()
    applicationPromise success application
    application
  }

  def createApplication(): Application with Engine

  def launch(createApplication: () => Application with Engine, args: Array[String]): Unit

  type Engine <: EngineImplementationContract
}

