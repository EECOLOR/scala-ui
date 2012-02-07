package ee.uiFramework.traits
import ee.uiFramework.components.Component

trait Constraints {self:Component =>
	var left:Long = 0
	var right:Long = 0
	var top:Long = 0
	var bottom:Long = 0
}