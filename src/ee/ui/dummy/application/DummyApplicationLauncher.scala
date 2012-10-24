package ee.ui.dummy.application

import ee.ui.application.ApplicationDependencies
import ee.ui.application.ApplicationLauncher
import ee.ui.dummy.nativeElements.DummyNativeManager
import ee.ui.events.NullEvent
import scala.actors.Actor
import scala.actors.TIMEOUT
import ee.ui.application.Application
import ee.ui.events.PulseEvent

trait DummyApplicationLauncher extends ApplicationLauncher {
  val applicationDependencies = new ApplicationDependencies {

    val launcher = DummyLauncher
    val applicationConstructor = createApplication _
    val nativeManager = DummyNativeManager
    val pulseEvent = DummyPulseEvent
  }
}

object DummyPulseEvent extends PulseEvent {
  val timer = new Actor {
    def act {
      loop {
        reactWithin(1000) {
          case TIMEOUT => fire
        }
      }
    }
  }
  timer start
}