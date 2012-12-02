package ee.ui.dummy.application

import ee.ui.application.ApplicationDependencies
import ee.ui.application.ApplicationLauncher
import ee.ui.dummy.nativeElements.DummyNativeManager
import ee.ui.events.NullEvent
import scala.actors.Actor
import scala.actors.TIMEOUT
import ee.ui.application.Application
import ee.ui.events.PulseEvent
import ee.ui.application.ClipBoard
import scala.collection.mutable
import ee.ui.application.DataFormat

trait DummyApplicationLauncher extends ApplicationLauncher {
  val applicationDependencies = new ApplicationDependencies {

    val launcher = DummyLauncher
    val applicationConstructor = createApplication _
    val nativeManager = DummyNativeManager
    val pulseEvent = DummyPulseEvent
    val systemClipBoard = new ClipBoard {
      val internalClipBoard = mutable.Map[DataFormat, Any]()
      def set(key: DataFormat, value: Any): Boolean =
        internalClipBoard.put(key, value) == Some(value)

      def get(key: DataFormat): Option[Any] =
        internalClipBoard get key

      def contains(key: DataFormat): Boolean =
        internalClipBoard contains key
    }
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
  timer.start
}