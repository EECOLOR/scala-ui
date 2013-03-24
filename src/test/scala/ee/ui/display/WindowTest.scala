package ee.ui.display

import org.specs2.mutable.Specification

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
  }
  
}