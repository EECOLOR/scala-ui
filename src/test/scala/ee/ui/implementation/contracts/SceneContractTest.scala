package ee.ui.implementation.contracts

import org.specs2.mutable.Specification

import ee.ui.display.detail.ReadOnlyRoot
import ee.ui.display.traits.RestrictedSize
import utils.SubtypeTest

object SceneContractTest extends Specification {
  
  xonly
  
  "SceneContract" should {
    
    "extend the correct types" in {
      SubtypeTest[SceneContract <:< ReadOnlyRoot with RestrictedSize]
    }
  }
}