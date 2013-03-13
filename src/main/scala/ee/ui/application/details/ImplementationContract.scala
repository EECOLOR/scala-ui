package ee.ui.application.details

import ee.ui.events.PulseEvent
import ee.ui.system.ClipBoard
import ee.ui.display.text.TextHelper
import ee.ui.display.implementation.DisplayImplementationHandler

trait ImplementationContract {
    val launcher:Launcher
    val pulseEvent:PulseEvent
    val displayImplementationHandler:DisplayImplementationHandler
    
    val systemClipBoard:ClipBoard
    val textHelper:TextHelper
}