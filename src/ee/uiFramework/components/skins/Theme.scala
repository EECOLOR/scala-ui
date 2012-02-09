package ee.uiFramework.components
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color
import ee.uiFramework.layouts.LayoutClient
import ee.uiFramework.shapes.Path
import ee.uiFramework.layouts.SimpleLayout
import ee.uiFramework.layouts.Center
import ee.uiFramework.layouts.Middle
import ee.uiFramework.shapes.Black

trait Theme {
   def backgroundRect:Rect
   def buttonUpSkin:ButtonSkinContract
   def buttonDownSkin:ButtonSkinContract
}

object Theme {
    implicit var current:Theme = DefaultTheme
}

object DefaultTheme extends Theme {
    
	val themeColor = Color(0xCCCCCC, .5)
	
    def backgroundRect = 
        new Rect with LayoutClient {
			percentWidth = 100
			percentHeight = 100
			fillColor = themeColor
			cornerRadius = 2
		}
	
	private def arrowSkin(arrowPath:String) = 
	    new ButtonSkinContract {
	      layout = SimpleLayout(horizontalAlign = Center, verticalAlign = Middle)
		  children(
		      backgroundRect,
			  new Path with LayoutClient {
				  left = 5
				  right = 5
				  top = 5
				  bottom = 5
				  
				  fillColor = Black
				  //triangle
				  val path = arrowPath
			  }
		   )
		}

	def buttonUpSkin = arrowSkin("M 8 0 L 16 16 L 0 16 L 8 0")
        
	def buttonDownSkin = arrowSkin("L 0 16 L 8 16 L 0 0")
	
}
