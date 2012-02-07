package ee.uiFramework.components
import ee.uiFramework.skins.SkinElement
import ee.uiFramework.skins.Skinnable
import ee.uiFramework.skins.Skin
import ee.uiFramework.State
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color
import ee.uiFramework.shapes.Path

class ComboBox[T] extends Component with Skinnable[ComboBoxSkinContract[T]] {
    val skin = new ComboBoxSkin[T]
}

trait ComboBoxSkinContract[T] extends Skin {
    val open:State = state()
    val closed:State = state()
    
    val text = skinElement[Text]
    val button = skinElement[Button]
    val list = skinElement[List[T]]
}

class ComboBoxSkin[T](implicit theme:Theme) extends ComboBoxSkinContract[T] {
	import ee.uiFramework.Environment.Graphics._

    layout = VerticalLayout
    
    children(
            new Container {
              layout = HorizontalLayout
              children(
            		text -> new Text,
            		button -> new LabelButton {
            		    override val skin = new LabelButtonSkinContract {
						    children(
						        theme.backgroundRect,
						        //triangle
						        new Path {
						            x = 5
						            y = 5
						            val path = List[DrawCommand]()
						        },
						        label -> new Label
						    )
            		        
            		    }
            		}
              )
            },
    		list -> new List[T]
    )
}
