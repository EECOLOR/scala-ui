package ee.ui.impl

import ee.ui.application.ImplicitDependencies
import annotation.unchecked.uncheckedVariance

trait NativeElement[+T <: NativeElement[T]] extends ImplicitDependencies { self:T =>

    //def createImplementation[U >: T <: NativeElement[U]](implicit m:NativeManager[U]):NativeManager[U]#NativeImplementationType = m.createImplementation(this)
    def createNativeElement[U >: T <: NativeElement[U]](implicit m:NativeManager[U,  _ <: NativeImplementation]):T = {
        m.createImplementation(this)
        this
    }
    
    /**
     * You need to override this method like this: 
     * 
     * def nativeElement = createNativeElement
     * 
     * This is needed to provide this trait with the correct context 
     */
    def nativeElement:T
    
    nativeElement
    
    def nativeImplementation[U >: T <: NativeElement[U], V  <: NativeImplementation](implicit m:NativeManager[U, V]):V = m.getImplementation(this)
}