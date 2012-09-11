package ee.ui.impl

import scala.collection.mutable

trait NativeManager[T <: NativeElement[T], I <: NativeImplementation] {
    type NativeImplementationType <: NativeImplementation
    
	private val _implementations = mutable.Map[T, I]()
    def createImplementation(element: T): I = _implementations.getOrElseUpdate(element, createInstance(element))
    def getImplementation(element: T): I = _implementations(element)
    
    protected def createInstance(element:T):I
}