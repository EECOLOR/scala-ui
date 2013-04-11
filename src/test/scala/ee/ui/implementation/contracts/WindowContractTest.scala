package ee.ui.implementation.contracts

import org.specs2.mutable.Specification
import utils.SignatureTest
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.Window
import utils.TypeTest
import utils.MemberTypeTest
import ee.ui.display.traits.ReadOnlyTitle
import ee.ui.display.traits.Size
import ee.ui.display.detail.ReadOnlyScene
import ee.ui.display.detail.ReadOnlyScene

object WindowContractTest extends Specification {
  xonly

  "WindowContract" should {
    "have a window property" in {
      MemberTypeTest[WindowContract, Size with ReadOnlyTitle with ReadOnlyScene].forMember(_.window)
    }
    "proxy window to the underlying window" in {
      val window = new Window
      val contract = WindowContract(window)
      contract.window === window
    }
  }
}
