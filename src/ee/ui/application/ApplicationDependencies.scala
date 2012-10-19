package ee.ui.application

import ee.ui.nativeImplementation.NativeManager

trait ApplicationDependencies {
    def launcher:Launcher
    def application:() => Application
    
    def nativeManager:NativeManager
}

trait ImplicitApplicationDependencies {
    import ApplicationDependencies.di
    
    implicit val implicitApplication:() => Application = di.application
    implicit val implicitLauncher:Launcher = di.launcher
}

object ApplicationDependencies extends ImplicitDependencies[ApplicationDependencies]