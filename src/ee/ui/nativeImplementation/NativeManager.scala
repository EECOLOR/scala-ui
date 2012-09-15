package ee.ui.nativeImplementation

import scala.collection.mutable
import ee.ui.nativeElements.NativeElement

trait NativeManager[T <: NativeElement[T], I <: NativeImplementation] {
    
	private val _implementations = mutable.Map[T, I]()
    def createImplementation(element: T): I = _implementations.getOrElseUpdate(element, createInstance(element))
    def getImplementation(element: T): I = _implementations(element)
    
    protected def createInstance(element:T):I
}