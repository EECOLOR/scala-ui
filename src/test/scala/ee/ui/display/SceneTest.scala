package ee.ui.display

import org.specs2.mutable.Specification

class SceneTest extends Specification {
  
  xonly
  isolated
  
  val scene = new Scene
  
  "Scene" should {
    "have a root property with default of None" in {
      scene.root.value === None
    }
    "be able to set a root value" in {
      val root = new Node
      scene.root = root
      scene.root.value === Some(root)
    }
    "be able to set a root value as an option" in {
      val root = new Node
      scene.root = Some(root)
          scene.root.value === Some(root)
    }
  }
}