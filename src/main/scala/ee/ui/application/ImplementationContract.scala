package ee.ui.application

import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.events.PulseEvent
import ee.ui.system.ClipBoard
import ee.ui.text.TextHelper

trait ImplementationContract {
    val launcher:Launcher
    val elementImplementationHandler:DisplayImplementationHandler
    val pulseEvent:PulseEvent
    val systemClipBoard:ClipBoard
    val textHelper:TextHelper
}