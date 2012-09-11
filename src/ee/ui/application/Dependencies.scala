package ee.ui.application

import ee.ui.impl.Managers
import ee.ui.impl.NativeManager
import ee.ui.impl.NativeImplementation

trait Dependencies {
    def launcher:Launcher
    def application:Application
    
    def windowManager:NativeManager[Window, _ <: NativeImplementation] 
	def stageManager:NativeManager[Stage, _ <: NativeImplementation] 
}

trait ImplicitDependencies {
    import ImplicitDependencies.di
    
    implicit def windowManager:NativeManager[Window, _ <: NativeImplementation] = di.windowManager 
	implicit def stageManager:NativeManager[Stage, _ <: NativeImplementation] = di.stageManager
    implicit val implicitApplication:() => Application = () => di.application
    implicit val implicitLauncher:Launcher = di.launcher
}

object ImplicitDependencies {
    var _di:Option[Dependencies] = None
    
	def set(di:Dependencies) =  _di = Some(di)
	
	def di:Dependencies = _di getOrElse {
        throw new Exception("Please call DependencyInjection.initialize before accessing properties")
    }
    
}
