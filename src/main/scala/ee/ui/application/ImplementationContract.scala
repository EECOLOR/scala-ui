package ee.ui.application

import ee.ui.nativeImplementation.ElementImplementationHandler
import ee.ui.events.PulseEvent

trait ImplementationContract {
    val launcher:Launcher
    val elementImplementationHandler:ElementImplementationHandler
    val pulseEvent:PulseEvent
    val systemClipBoard:ClipBoard
}