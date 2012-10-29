package ee.ui.primitives

import ee.ui.properties.Property

sealed abstract class Camera

class PerspectiveCamera(defaultFieldOfView:Double = 30d) extends Camera {
	val _fieldOfView = new Property(defaultFieldOfView)
	def fieldOfView = _fieldOfView
	def fieldOfView_=(value:Double) = fieldOfView.value = value
}

object ParallelCamera extends Camera