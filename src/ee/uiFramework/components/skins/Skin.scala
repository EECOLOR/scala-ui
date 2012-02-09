package ee.uiFramework.skins
import ee.uiFramework.components.Container
import scala.collection.mutable.ListBuffer
import ee.uiFramework.components.Component

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
	
	class SkinElement[T <: Component] {
		private var _content:Option[T] = None
				
				def content = _content
				
				def -> (element:T):T = {
						_content = Some(element)
								element
				}
	}
}