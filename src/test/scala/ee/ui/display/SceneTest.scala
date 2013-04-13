package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.display.shapes.Rectangle
import utils.SubtypeTest
import ee.ui.implementation.contracts.SceneContract

class SceneTest extends Specification {
  
  xonly
  isolated
  
  val scene = new Scene
  val root = new Rectangle {}

  "Scene" should {
    "extend the correct types" in {
      SubtypeTest[Scene <:< SceneContract]
    }    
    "have a root property with default of None" in {
      scene.root.value === None
    }
    "be able to set a root value" in {
      scene.root = root
      scene.root.value === Some(root)
    }
    "be able to set a root value as an option" in {
      scene.root = Some(root)
          scene.root.value === Some(root)
    }
  }
}