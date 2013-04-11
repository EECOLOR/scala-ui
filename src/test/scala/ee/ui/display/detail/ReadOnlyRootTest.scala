package ee.ui.display.detail

import org.specs2.mutable.Specification
import utils.SignatureTest
import ee.ui.members.ReadOnlyProperty
import scala.language.existentials
import ee.ui.display.traits.ReadOnlySize

object ReadOnlyRootTest extends Specification {
  
  xonly
  
  "ReadOnlyRoot" should {
    "have a root property" in {
      SignatureTest[ReadOnlyRoot, ReadOnlyProperty[Option[~]] forSome { type ~ <: NodeContract}](_.root)
    }
  }
}