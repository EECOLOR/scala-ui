package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess

class WindowTest extends Specification {

  xonly
  isolated
  
  val window = new Window
  
  "Window" should {
    
    "have an scene property with a default of None" in {
      window.scene.value === None 
    }
    
    "be able to set a scene" in {
      window.scene = new Scene
    }
    
    "have a showing property that is false by default" in {
      window.showing.value === false 
    }
    
    "should be able to show and hide" in {
      var result = false
      window.showing.change { result = _ }
      
      Window.show(window)
      val shown = result === true
      
      Window.hide(window)
      val hidden = result === false
      
      shown and hidden
    }
  }
  
}