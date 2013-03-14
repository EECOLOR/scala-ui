package ee.ui.application

import ee.ui.application.details.ApplicationDependencies
import ee.ui.application.details.ImplementationContract
import ee.ui.application.details.Launcher
import ee.ui.events.PulseEvent
import ee.ui.system.ClipBoard
import ee.ui.display.text.TextHelper
import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.members.Event
import scala.actors.Actor
import scala.actors.TIMEOUT

abstract class TestEngineLauncher extends ApplicationLauncher {

  val applicationDependencies = new ApplicationDependencies {

    val implementationContract = new ImplementationContract {

      val launcher: Launcher = new Launcher {

        val launchComplete = Event[Application]

        def launch(args: Array[String])(implicit createApplication: () => Application): Unit = {

          val application = createApplication()

          application.init()

          application.start()

          launchComplete fire application
        }
        
        def exit(application:Application) = {
            application.stop()
        }
      }

      val pulseEvent: PulseEvent = new PulseEvent { self =>

        val timer = new Actor {
          def act =
            loop(
              reactWithin(100) {
                case TIMEOUT => self.fire()
              })
        }
        timer.start
      }

      val displayImplementationHandler: DisplayImplementationHandler = ???

      val systemClipBoard: ClipBoard = ???

      // Text and font helpers
      val textHelper: TextHelper = ???
    }
  }
}