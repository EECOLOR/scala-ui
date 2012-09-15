package ee.ui.application
trait ImplicitDependencies[T] {
    var _di:Option[T] = None
    
	def set(di:T) =  _di = Some(di)
	
	def di:T = _di getOrElse {
        throw new Exception(s"Please call $this.set before accessing properties")
    }
    
}
