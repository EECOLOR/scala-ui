package ee.uiFramework.components
import ee.uiFramework.skins.Skin
import ee.uiFramework.skins.Skinnable
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color
import ee.uiFramework.layouts.LayoutClient

class Button extends Component with Skinnable[ButtonSkinContract] {
	val skin:ButtonSkinContract = new ButtonSkin

}

trait ButtonSkinContract extends Skin {
	val up = state
	val over = state
	val down = state
}

class ButtonSkin(implicit theme:Theme) extends ButtonSkinContract {

    children(
    	theme.backgroundRect
    )
}

class LabelButton extends Button with Skinnable[LabelButtonSkinContract] {
	override val skin:LabelButtonSkinContract = new LabelButtonSkin
	
	def label = skin.label.text
	def label_=(label:String) = skin.label.text = label
	
}

trait LabelButtonSkinContract extends ButtonSkinContract {
	val label = skinElement[Label]
}

class LabelButtonSkin(implicit theme:Theme) extends LabelButtonSkinContract {
	
	children (
		theme.backgroundRect,
				
		label -> new Label with LayoutClient {
			left::up > 2
			top::up > 2
					
			left::over > 2
			top::over > 2
					
			left::down > 4
			top::down > 4
		}
	)
}