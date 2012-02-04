package ee.uiFramework.components
import ee.uiFramework.skins.SkinElement
import ee.uiFramework.skins.Skinnable
import ee.uiFramework.skins.Skin
import ee.uiFramework.State
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color
import ee.uiFramework.shapes.Path

class ComboBox[T] extends Skinnable[ComboBoxSkinContract[T]] {
    val defaultSkin = new ComboBoxSkin[T]
}

trait ComboBoxSkinContract[T] extends Skin {
    val open:State = state()
    val closed:State = state()
    
    val text = new SkinElement[Text]
    val button = new SkinElement[Button]
    val list = new SkinElement[List[T]]
}

class ComboBoxSkin[T](implicit theme:Theme) extends ComboBoxSkinContract[T] {
    
    layout = VerticalLayout
    
    children(
            new Container {
              layout = HorizontalLayout
              children(
            		text -> new Text,
            		button -> new Button {
            		    skin = new ButtonSkinContract {
						    children(
						        theme.backgroundRect,
						        //triangle
						        new Path {
						            x = 5
						            y = 5
						            
						        }
						    )
            		        
            		    }
            		}
              )
            },
    		list -> new List[T]
    )
}