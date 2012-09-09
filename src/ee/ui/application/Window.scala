package ee.ui.application

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty

trait Window {
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