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
import ee.ui.application.ImplementationContract
import ee.ui.application.TextHelper
import ee.ui.primitives.Point
import ee.ui.nativeElements.Text

trait DummyApplicationLauncher extends ApplicationLauncher {
  val applicationDependencies = new ApplicationDependencies {

    val implementationContract = new ImplementationContract {

      val launcher = DummyLauncher
      val elementImplementationHandler = DummyNativeManager
      val pulseEvent = DummyPulseEvent
      val systemClipBoard = new ClipBoard {
        val internalClipBoard = mutable.Map[DataFormat, AnyRef]()
        def set(key: DataFormat, value: AnyRef): Boolean =
          internalClipBoard.put(key, value) == Some(value)

        def get(key: DataFormat): Option[AnyRef] =
          internalClipBoard get key

        def contains(key: DataFormat): Boolean =
          internalClipBoard contains key
      }
      val textHelper = new TextHelper {
        def getCaretPosition(text: Text, index: Int): Point = Point(0, 0)
        def getCaretIndex(text: Text, position: Point): Int = 0
      }
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