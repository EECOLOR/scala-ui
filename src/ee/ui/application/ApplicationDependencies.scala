package ee.ui.application

import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Stage
import ee.ui.events.PulseEvent

trait ApplicationDependencies {
    def launcher:Launcher
    def applicationConstructor:() => Application
    def nativeManager:NativeManager
    def pulseEvent:PulseEvent
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
