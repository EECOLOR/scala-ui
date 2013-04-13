package ee.ui.display.detail

import org.specs2.mutable.Specification

import ee.ui.implementation.contracts.SceneContract
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest

object ReadOnlySceneTest extends Specification {
  
  xonly
  
  "ReadOnlyScene" should {
    
    "have a scene property" in {
      SignatureTest[ReadOnlyScene, ReadOnlyProperty[Option[SceneContract]]](_.scene)
    }
  }
}