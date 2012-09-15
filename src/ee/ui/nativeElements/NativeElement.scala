package ee.ui.nativeElements

import ee.ui.application.ImplicitDependencies
import ee.ui.nativeImplementation.NativeImplementation
import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeImplementation.ImplicitNativeManagerDependencies

trait NativeElement[+T <: NativeElement[T]] extends ImplicitNativeManagerDependencies { self:T =>

    protected class Proof[+P >: T <: NativeElement[P]]
    
    private var created = false
    
    def createNativeElement[U >: T <: NativeElement[U]](implicit m:NativeManager[U,  _ <: NativeImplementation]):Proof[U] = {
        if (created) sys.error("Can not call createNativeElement twice") 
        else {
        	m.createImplementation(this)
        	created = true
        	new Proof[U]
        }
    }
    
    /**
     * You need to override this method like this: 
     * 
     * def nativeElement = createNativeElement
     * 
     * This is needed to provide this trait with the correct context 
     */
    def nativeElement:Proof[T]
    
    nativeElement
    
    def nativeImplementation[U >: T <: NativeElement[U], V  <: NativeImplementation](implicit m:NativeManager[U, V]):V = m.getImplementation(this)
}