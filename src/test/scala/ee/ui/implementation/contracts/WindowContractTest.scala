package ee.ui.implementation.contracts

import org.specs2.mutable.Specification
import utils.SignatureTest
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.Window
import utils.TypeTest
import utils.MemberTypeTest
import ee.ui.display.traits.ReadOnlyTitle
import ee.ui.display.traits.Size

object WindowContractTest extends Specification {
  xonly

  "WindowContract" should {
    "have a reads property" in {
      MemberTypeTest[WindowContract, Size with ReadOnlyTitle].forMember(_.window)
    }
    "proxy reads to the underlying window" in {
      val window = new Window
      val contract = WindowContract(window)
      contract.window === window
    }
  }
}
