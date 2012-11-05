package ee.ui.application

import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Stage
import ee.ui.events.PulseEvent
import ee.ui.layout.LayoutEngine
import ee.ui.layout.DefaultLayoutEngine

trait ApplicationDependencies {
    val launcher:Launcher
    val applicationConstructor:() => Application
    val nativeManager:NativeManager
    val pulseEvent:PulseEvent
    lazy val layoutEngine:LayoutEngine = DefaultLayoutEngine
}

object ApplicationDependencies extends Dependencies[ApplicationDependencies]

trait ImplicitLauncher {
	implicit def implicitLauncher:Launcher = ApplicationDependencies.di.launcher
}
trait ImplicitApplicationConstructor {
	implicit def implicitApplication:() => Application = ApplicationDependencies.di.applicationConstructor
}
trait ImplicitNativeManager {
	implicit def implicitNativeManager:NativeManager = ApplicationDependencies.di.nativeManager
}
trait ImplicitPulseEvent {
	implicit def implicitPulseEvent:PulseEvent = ApplicationDependencies.di.pulseEvent
}
trait ImplicitLayoutEngine {
  implicit def layoutEngine:LayoutEngine = ApplicationDependencies.di.layoutEngine
}
