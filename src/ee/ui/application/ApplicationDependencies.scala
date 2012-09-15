package ee.ui.application

import ee.ui.nativeImplementation.NativeManagerDependencies
import ee.ui.nativeImplementation.NativeManager

trait ApplicationDependencies {
    def launcher:Launcher
    def application:() => Application
    
    def nativeManagers:NativeManagerDependencies
}

trait ImplicitApplicationDependencies {
    import ApplicationDependencies.di
    
    implicit val implicitApplication:() => Application = di.application
    implicit val implicitLauncher:Launcher = di.launcher
}

object ApplicationDependencies extends ImplicitDependencies[ApplicationDependencies] {
  
  override def set(di:ApplicationDependencies) = {
	  super.set(di)
	  NativeManagerDependencies set di.nativeManagers
  }
  
}