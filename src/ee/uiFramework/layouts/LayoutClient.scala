package ee.uiFramework.layouts
import ee.uiFramework.components.Component

trait LayoutClient {self:Component =>
	var left:Long = 0
	var right:Long = 0
	var top:Long = 0
	var bottom:Long = 0
	var percentWidth:Long = 0
	var percentHeight:Long = 0
}