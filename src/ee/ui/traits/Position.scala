package ee.ui.traits

import ee.ui.properties.Property

trait Position {
	private val _x = new Property(0d)
	def x = _x
	def x_=(value:Double) = x.value = value
	
	private val _y = new Property(0d)
	def y = _y
	def y_=(value:Double) = y.value = value
}