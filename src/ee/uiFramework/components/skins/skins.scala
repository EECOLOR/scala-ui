package ee.uiFramework.skins
import ee.uiFramework.State
import scala.collection.mutable.ListBuffer
import ee.uiFramework.components.Container
import ee.uiFramework.components.Component
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color
import ee.uiFramework.components.Theme

trait Skinnable[+T <: Skin] extends Component {
    val defaultSkin:T
    
    skin = defaultSkin
    
    private var _skin:T = _
    
    def skin:T = _skin
    def skin_=(skin:T) = _skin = skin
    
    implicit def skinElementToContent[T <: Component](skinElement:SkinElement[T]):T =  
		skinElement.content match {
		    	case Some(element) => element
		    	case None => throw new Exception(
"""Skin element was not initialized. 
Make sure you use the a syntax like this when assigning children: label -> new Label
An example:

class SomeSkin implements SomeSkinContract {
	val label = new SkinElement[Label](this)
		    	        
	children(
		label -> new Label
	)
}
""")}
}

trait Skin extends Container {
    val skinElements = ListBuffer[SkinElement[_]]()
    
	def state():State = new State{}
	
	def skinElement[T <: Component]:SkinElement[T] = {
	    val skinElement = new SkinElement[T]
	    skinElements += skinElement
	    skinElement
	}
	
	def validSkin = {
	    skinElements.exists {_.content.isDefined}  
	}
}

class SkinElement[T <: Component] {
	private var _content:Option[T] = None
			
	def content = _content
	
	def -> (element:T):T = {
		_content = Some(element)
		element
	}
}
