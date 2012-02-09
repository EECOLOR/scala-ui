package ee.uiFramework.components
import ee.uiFramework.skins.Skinnable
import ee.uiFramework.skins.Skin
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color
import ee.uiFramework.shapes.Path
import ee.uiFramework.layouts.VerticalLayout

class ComboBox[T] extends Component with Skinnable[ComboBoxSkinContract[T]] {
    val skin = new ComboBoxSkin[T]
}

trait ComboBoxSkinContract[T] extends Skin {
    val open = state()
    val closed = state()
    
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
            		button -> new Button {
            		    override val skin = theme.buttonDownSkin 
            		}
              )
            },
    		list -> new List[T]
    )
}
