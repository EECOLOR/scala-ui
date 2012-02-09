package ee.uiFramework.layouts
import ee.uiFramework.components.Container

class SimpleLayout(
        val horizontalAlign:HorizontalAlign = Left,
        val verticalAlign:VerticalAlign = Top) extends Layout {
    
    /**
     * Only takes layout clients into account
     */
	def layoutChildren(container:Container) = {}
}

object SimpleLayout {
    def apply(horizontalAlign:HorizontalAlign, verticalAlign:VerticalAlign) = new SimpleLayout(horizontalAlign, verticalAlign) 
    def apply(horizontalAlign:HorizontalAlign) = new SimpleLayout(horizontalAlign = horizontalAlign) 
    def apply(verticalAlign:VerticalAlign) = new SimpleLayout(verticalAlign = verticalAlign) 
}