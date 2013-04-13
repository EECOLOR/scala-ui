package ee.ui.implementation.contracts

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.display.detail.ReadOnlyScene
import ee.ui.display.traits.ReadOnlyTitle
import ee.ui.display.traits.Size
import utils.SignatureTest
import ee.ui.system.RestrictedAccess

class WindowContractTest extends Specification {

  xonly
  isolated

  val window = new Window
  val contract = WindowContract(window)

  "WindowContract" should {

    "have a window property" in {
      SignatureTest[WindowContract, Size with ReadOnlyTitle with ReadOnlyScene](_.window)
    }

    "proxy window to the underlying window" in {
      contract.window === window
    }

    "give retricted access to the unerlying window" in {
      WindowContract.internalWindow(contract)(RestrictedAccess) === window
    }
  }
}
