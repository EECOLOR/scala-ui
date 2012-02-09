package ee.uiFramework.components
import ee.uiFramework.layouts.Layout
import ee.uiFramework.layouts.NoLayout
import ee.uiFramework.layouts.SimpleLayout

trait Container extends Component {
    var layout:Layout = NoLayout
	def children(components:Component*) = {}
    
    def addChild(child:Component) = {}
}

object Container {
    /**
     * Constructs a container with default layout (NoLayout)
     */
    def apply(components:Component*) = {
        new Container {
            children(components:_*)
        }
    }
}