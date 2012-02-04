package ee.uiFramework.components
import ee.uiFramework.skins.Skin
import ee.uiFramework.skins.Skinnable
import ee.uiFramework.State
import ee.uiFramework.skins.SkinElement
import ee.uiFramework.Constraints
import ee.uiFramework.shapes.Rect
import ee.uiFramework.shapes.Color

class Button extends Skinnable[ButtonSkinContract] {
	val defaultSkin = new ButtonSkin
			
}

class LabelButton extends Button with Skinnable[LabelButtonSkinContract] {
	val defaultSkin = new LabelButtonSkin
	
	def label = skin.label.text
	def label_=(label:String) = skin.label.text = label
}

trait ButtonSkinContract extends Skin {
	val up = state
	val over = state
	val down = state
}

trait LabelButtonSkinContract extends ButtonSkinContract {
	val label = skinElement[Label]
}

class ButtonSkin(implicit theme:Theme) extends ButtonSkinContract {

    children(
    	theme.backgroundRect
    )
}

class LabelButtonSkin(implicit theme:Theme) extends LabelButtonSkinContract {
	
	children (
		theme.backgroundRect,
				
		label -> new Label with Constraints {
			left::up > 2
			top::up > 2
					
			left::over > 2
			top::over > 2
					
			left::down > 4
			top::down > 4
		}
	)
}
