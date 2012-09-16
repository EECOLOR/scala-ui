package ee.ui.nativeImplementation

import scala.collection.mutable
import ee.ui.nativeElements.NativeElement

trait NativeManager[T <: NativeElement[T], I <: NativeImplementation] {
    
	private val _implementations = mutable.Map[T, I]()
	
    def createImplementation(element: T): I = {
	  val implementation = createInstance(element)
	  //add it to the implementations map
	  _implementations += element -> implementation
	  //now it's safe to init because it can be found in the implementations
	  implementation.init
	  implementation
	}
	
    def getImplementation(element: T): I = _implementations(element)
    
    protected def createInstance(element:T):I
}