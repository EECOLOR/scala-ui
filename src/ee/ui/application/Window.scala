package ee.ui.application

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.impl.NativeElement
import ee.ui.impl.NativeManager
import ee.ui.impl.Managers
import ee.ui.impl.NativeImplementation
import ee.ui.impl.NativeManager

class Window extends NativeElement[Window] {
    
    def nativeElement = createNativeElement
    
	protected val writableShowing = new Property(false)
	lazy val showing:ReadOnlyProperty[Boolean] = writableShowing
	
	writableShowing onChangeMatch {
        case (false, true, p) => showWindow
        case (true, false, p) => hideWindow
    }

    protected def showWindow() = {}
    protected def hideWindow() = {}	
    
    val _width = new Property(Double.NaN)
    def width = _width
    def width_=(value:Double) = width.value = value
    
    val _height = new Property(Double.NaN)
    def height = _height
    def height_=(value:Double) = height.value = value
}