package ee.uiFramework.skins
import ee.uiFramework.components.Component

trait Skinnable[+T <: Skin] {
    
    val skin:T
    
    import skin._
    
    implicit def skinElementToContent[T <: Component](skinElement:SkinElement[T]):T =  
		skinElement.content match {
		    	case Some(element) => element
		    	case None => throw new Exception(
"""Skin element was not initialized. 
Make sure you use the a syntax like this when assigning children: label -> new Label
An example:

class SomeSkin implements SomeSkinContract {
	val label = new SkinElement[Label]
		    	        
	children(
		label -> new Label
	)
}
""")}
}


