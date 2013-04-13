package ee.ui.display.detail

import org.specs2.mutable.Specification

import ee.ui.display.implementation.contracts.NodeContract
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest

object ReadOnlyRootTest extends Specification {
  
  xonly
  
  "ReadOnlyRoot" should {
    
    "have a root property" in {
      SignatureTest[ReadOnlyRoot, ReadOnlyProperty[Option[NodeContract]]](_.root)
    }
  }
}