package ee.ui.traits

import ee.ui.properties.Property

trait Position {
	private val _x = new Property(Double.NaN)
	def x = _x
	def x_=(value:Double) = x.value = value
	
	private val _y = new Property(Double.NaN)
	def y = _y
	def y_=(value:Double) = y.value = value
}