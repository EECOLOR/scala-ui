package ee.ui.system

import org.specs2.mutable.Specification
import utils.TypeTest
import ee.ui.members.Property

class PlatformTest extends Specification {
  xonly
  isolated
  
  "Platform" should {
    "have an implicitExit property" in {
      TypeTest[Property[Boolean]].forInstance(new Platform().implicitExit)
    }
  }
}