package ee.uiFramework.components
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color

trait Theme {
   val backgroundRect:Rect
}

object Theme {
    implicit var current:Theme = DefaultTheme
}

object DefaultTheme extends Theme {
    
	val themeColor = Color(0xCCCCCC, .5)
	
    lazy val backgroundRect = {
	    new Rect {
			percentWidth = 100
			percentHeight = 100
			fillColor = themeColor
			cornerRadius = 2
		}
	} 
        
}
