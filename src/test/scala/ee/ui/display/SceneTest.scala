package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.display.shapes.Rectangle
import ee.ui.implementation.contracts.SceneContract
import utils.SubtypeTest
import utils.SignatureTest
import ee.ui.members.Property
import ee.ui.display.implementation.contracts.NodeContract

class SceneTest extends Specification {

  xonly
  isolated

  val scene = new Scene
  val root = new Rectangle {}
  def correctRootValue = scene.root.value === Some(root)
  
  "Scene" should {

    "extend the correct types" in {
      SubtypeTest[Scene <:< SceneContract]
    }

    "have a root property" >> {

      "with the correct signature" in {
        SignatureTest[Scene, Property[Option[NodeContract]]](_.root)
      }
        
      "with default of None" in {
        scene.root.value === None
      }

      "that can be set directly" in {
        scene.root = root
        correctRootValue
      }

      "that can be set as an option" in {
        scene.root = Some(root)
        correctRootValue
      }
    }

  }
}