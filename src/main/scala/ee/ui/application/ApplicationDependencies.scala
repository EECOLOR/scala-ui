package ee.ui.application

import ee.ui.nativeImplementation.ElementImplementationHandler
import ee.ui.events.PulseEvent
import ee.ui.layout.LayoutEngine
import ee.ui.layout.DefaultLayoutEngine

trait ApplicationDependencies {
    lazy val layoutEngine:LayoutEngine = DefaultLayoutEngine
    val implementationContract:ImplementationContract
}

object ApplicationDependencies extends Dependencies[ApplicationDependencies]

trait ImplicitLauncher {
	implicit def implicitLauncher:Launcher = ApplicationDependencies.di.implementationContract.launcher
}
trait ImplicitNativeManager {
	implicit def implicitNativeManager:ElementImplementationHandler = ApplicationDependencies.di.implementationContract.elementImplementationHandler
}
trait ImplicitPulseEvent {
	implicit def implicitPulseEvent:PulseEvent = ApplicationDependencies.di.implementationContract.pulseEvent
}
trait ImplicitSystemClipBoard {
	implicit def implicitSystemClipBoard:ClipBoard = ApplicationDependencies.di.implementationContract.systemClipBoard
}
trait ImplicitLayoutEngine {
  implicit def implicitLayoutEngine:LayoutEngine = ApplicationDependencies.di.layoutEngine
}
trait ImplicitTextHelper {
  implicit def implicitTextHelper:TextHelper = ApplicationDependencies.di.implementationContract.textHelper
}
