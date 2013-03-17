package ee.ui.application

import ee.ui.events.PulseEvent
import ee.ui.display.Window
import scala.collection.mutable.ListBuffer
import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.layout.LayoutEngine
import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.application.details.ApplicationDependencies
import ee.ui.application.details.PulseHandler
import ee.ui.system.Platform
import ee.ui.members.ObservableArrayBuffer
import ee.ui.system.RestrictedAccess

abstract class Application extends EngineDependencies {

  private val _windows = ObservableArrayBuffer[Window]()

  val windowsChange = _windows.change
  def windows = _windows.toSeq

  protected def show(window: Window)(implicit windowImplementationHandler: WindowImplementationHandler): Unit = {
    windowImplementationHandler show window
    _windows += window
    implicit val access = RestrictedAccess
    Window show window
  }

  protected def hide(window: Window)(implicit windowImplementationHandler: WindowImplementationHandler): Unit = {
    windowImplementationHandler hide window
    implicit val access = RestrictedAccess
    Window hide window
    _windows -= window
  }
  
  def init(): Unit = {}

  def start(): Unit = {
    // Create primary window and call application start method
    val primaryWindow = new Window(true)
    start(primaryWindow)
  }

  protected def start(window: Window): Unit
  
  protected def exit()(implicit platform:Platform):Unit = platform.exit() 
  
  def stop(): Unit = {}
}

object Application {

  def launch(args: Array[String])(
    implicit createApplication: () => Application, applicationDependencies: ApplicationDependencies): Unit = {

    val implementationContract = applicationDependencies.implementationContract

    val launcher = implementationContract.launcher
    launcher launchComplete { application =>

      //create a new pulse handler for this application
      val pulseHandler = applicationDependencies.createPulseHandler(application)
      
      //connect the pulse event to the pulse handler
      val pulseEvent = implementationContract.pulseEvent
      pulseEvent(pulseHandler.pulse)
      //fire the first pulse
      pulseEvent.fire
    }
    launcher launch args
  }
  
  def hide(application:Application, window:Window)(implicit windowImplementationHandler: WindowImplementationHandler) = 
    application hide window
}