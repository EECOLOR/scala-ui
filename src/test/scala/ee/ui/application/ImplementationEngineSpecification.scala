package ee.ui.application

import org.specs2.Specification
import ee.ui.application.details.ApplicationDependencies
import ee.ui.application.details.ImplementationContract
import ee.ui.layout.LayoutEngine
import ee.ui.application.details.PulseHandler
import ee.ui.layout.DefaultLayoutEngine
import ee.ui.application.details.DefaultPulseHandler
import ee.ui.application.details.Launcher
import ee.ui.events.PulseEvent
import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.system.ClipBoard
import ee.ui.display.text.TextHelper

object ImplementationEngine extends Specification {

  def is = "Implementation engine specification".title ^
    hide ^ end
  //show ^ end

  def hide = args(xonly = true) ^ show ^ end

  def show =
    """ Introduction
    
      An implementation engine will supply the following elements that are specific to the 
      operating system (or device) that the framework is used on. It will provide elements 
      like rendering, text display, clipboard access etc.
      """ ^ "The first step is to create an engine specific launcher" ^
      { // Engine specific launcher

        abstract class TestEngineLauncher extends ApplicationLauncher {

        }

        ok
      } ^ "The ApplicationLauncher requires the application to fulfill the ApplicationDependencies" ^
      { // Application dependencies

        abstract class TestEngineLauncher extends ApplicationLauncher {

          lazy val applicationDependencies = new ApplicationDependencies {

            val implementationContract: ImplementationContract = ???

            // allows you to override the default LayoutEngine implementation
            override val layoutEngine: LayoutEngine = DefaultLayoutEngine

            // allows you to override the default PulseHandler provider
            override val createPulseHandler: (Application) => PulseHandler =
              { application =>
                new DefaultPulseHandler(application, implementationContract.displayImplementationHandler, layoutEngine)
              }
          }
        }

        ok
      } ^ "The ImplementationContract specifies the mandatory components that the engine needs to provide" ^
      { // Implementation contract

        lazy val implementationContract =
          new ImplementationContract {
            // The launcher launches the actual application
            val launcher: Launcher = ???

            /* 
             The pulse event is fired when the application needs to perform an update cycle. Check 
             the DefaultPulseHandler for more information
            */
            val pulseEvent: PulseEvent = ???

            // This handles all display related elements, Text, Group, Rectangle, etc.
            val displayImplementationHandler: DisplayImplementationHandler = ???

            // Provides the clipboard
            val systemClipBoard: ClipBoard = ???

            // Text and font helpers
            val textHelper: TextHelper = ???
          }

        ok
      } ^ p ^ "The launcher launches" ^
      end
}