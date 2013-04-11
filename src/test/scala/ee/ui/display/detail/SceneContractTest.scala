package ee.ui.display.detail

import org.specs2.mutable.Specification
import utils.SignatureTest
import ee.ui.members.ReadOnlyProperty
import scala.language.existentials

object SceneContractTest extends Specification {
  
  xonly
  
  "SceneContract" should {
    "have a scene property" in {
      SignatureTest[ReadOnlyScene, ReadOnlyProperty[Option[~]] forSome { type ~ <: ReadOnlyScene#SceneType }](
        _.scene)
    }
  }
}