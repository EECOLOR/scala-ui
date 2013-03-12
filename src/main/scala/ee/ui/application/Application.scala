package ee.ui.application

import ee.ui.events.PulseEvent
import ee.ui.display.Window
import scala.collection.mutable.ListBuffer
import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.layout.LayoutEngine
import ee.ui.display.implementation.WindowImplementationHandler

abstract class Application {
  
  implicit val windowImplementationHandler: WindowImplementationHandler
  
  def show(window: Window): Unit = {
    Window show window
  }

  def hide(window: Window): Unit = {
    Window hide window
  }

  def windows = Window.windows

  def init(): Unit = {}

  def start(): Unit = {
    // Create primary stage and call application start method
    val primaryWindow = new Window(true)
    start(primaryWindow)
  }

  def start(window: Window): Unit
  def stop(): Unit = {}
}

object Application {

  def launch(args: Array[String])(implicit launcher: Launcher, createApplication: () => Application, pulseEvent: PulseEvent, displayImplementationHandler: DisplayImplementationHandler, layoutEngine: LayoutEngine): Unit = {
    launcher launchComplete {
      println("launchComplete")
    }
    launcher launchComplete createPulseHandler _
    launcher launch args
  }

  def createPulseHandler(application: Application)(implicit pulseEvent: PulseEvent, displayImplementationHandler: DisplayImplementationHandler, layoutEngine: LayoutEngine) = {
    val pulseHandler = new PulseHandler(application)
    pulseEvent(pulseHandler.pulse)
    pulseEvent.fire
  }
}