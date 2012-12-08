package ee.ui.application

import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.events.PulseEvent
import ee.ui.layout.LayoutEngine
import ee.ui.layout.DefaultLayoutEngine

trait ApplicationDependencies {
    lazy val layoutEngine:LayoutEngine = DefaultLayoutEngine
    val implementationContract:ImplementationContract
}
