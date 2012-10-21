package ee.ui.application

import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Stage
import ee.ui.events.PulseEvent

trait ApplicationDependencies {
    def launcher:Launcher
    def application:() => Application
    def nativeManager:NativeManager
    def pulseEvent:PulseEvent
}

trait ImplicitApplicationDependencies {
    import ApplicationDependencies.di
    
    implicit def implicitApplication:() => Application = di.application
    implicit def implicitLauncher:Launcher = di.launcher
    implicit def implicitNativeManager:NativeManager = di.nativeManager
    implicit def implicitPulseEvent:PulseEvent = di.pulseEvent
}

object ApplicationDependencies extends ImplicitDependencies[ApplicationDependencies]