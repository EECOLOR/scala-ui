package ee.uiFramework.components
import ee.uiFramework.skins.Skinnable
import ee.uiFramework.skins.Skin
import ee.uiFramework.traits.DataList
import scala.{List => ScalaList}
import ee.uiFramework.layouts.VerticalLayout

class List[T] extends Component with Skinnable[ListSkinContract[T]] {
	val skin = new ListSkin[T]

	def processDataList(dataList:ScalaList[T]) = 
    dataList.foreach(skin.itemRenderer andThen skin.container.addChild)
}

trait ListSkinContract[T] extends Skin {
    val container = skinElement[Container]
    val itemRenderer:(T => Component)
}

class ListSkin[T] extends ListSkinContract[T] {
    
    children(
    	container -> new Container {
    		layout = VerticalLayout
    	}
    )
    
    val itemRenderer = { item: T => new ListItem[T](item) }

	implicit def defaultLabelExtractor(item:T):String = {
	    type TextType = {val text:String}
	    item.asInstanceOf[TextType].text
	}
}

class ListItem[T <% String](item:T) extends Component with Skinnable[ListItemSkinContract] {
	val skin = new ListItemSkin
	
	skin.text.text = item
}

trait ListItemSkinContract extends Skin {
    val text:Text
}

class ListItemSkin extends ListItemSkinContract {
    val text = new Text
}	
