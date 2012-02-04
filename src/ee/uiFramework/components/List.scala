package ee.uiFramework.components
import ee.uiFramework.skins.Skinnable
import ee.uiFramework.skins.Skin
import ee.uiFramework.traits.DataList
import scala.{List => ScalaList}

class List[T] extends Skinnable[ListSkinContract[T]] with DataList[T] {
	val defaultSkin = new ListSkin[T]
	
	def processDataList(dataList:ScalaList[T]) = 
	    dataList.foreach(skin.itemRenderer andThen skin.container.addChild)
}

trait ListSkinContract[T] extends Skin {
    val container:Container
    val itemRenderer:(T => Component)
}

class ListSkin[T] extends ListSkinContract[T] {
    
    val container = new Container {
        layout = VerticalLayout
    }
    
    val itemRenderer = { item: T => new ListItem[T](item) }

	implicit def defaultLabelExtractor(item:T):String = {
	    type TextType = {val text:String}
	    item.asInstanceOf[TextType].text
	}
}

class ListItem[T <% String](item:T) extends Skinnable[ListItemSkinContract] {
	val defaultSkin = new ListItemSkin
	
	skin.text.text = item
}

trait ListItemSkinContract extends Skin {
    val text:Text
}

class ListItemSkin extends ListItemSkinContract {
    val text = new Text
}	