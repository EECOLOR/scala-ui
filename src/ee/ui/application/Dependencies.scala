package ee.ui.application

trait Dependencies {
    def launcher:Launcher
    def application:Application
    def stage:Boolean => Stage
}

trait ImplicitDependencies {
    import ImplicitDependencies.di
    
    implicit val stage:Boolean => Stage = di.stage
    implicit val application:() => Application = () => di.application
    implicit val launcher:Launcher = di.launcher
}

object ImplicitDependencies {
    var _di:Option[Dependencies] = None
    
	def set(di:Dependencies) =  _di = Some(di)
	
	def di:Dependencies = _di getOrElse {
        throw new Exception("Please call DependencyInjection.initialize before accessing properties")
    }
    
}
