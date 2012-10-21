package ee.ui.traits

import ee.ui.properties.Property

trait Size {
    private val _width = new Property(Double.NaN)
    def width = _width
    def width_=(value:Double) = width.value = value
    
    private val _height = new Property(Double.NaN)
    def height = _height
    def height_=(value:Double) = height.value = value
}