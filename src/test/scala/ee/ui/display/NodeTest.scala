package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.Property
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.detail.ReadOnlyNode

class NodeTest extends Specification {

  xonly
  isolated

  val node = new Node {}
  
  "Node" should {
    "extends the correct traits" in {
      node must beAnInstanceOf[ReadOnlyNode]
    }
  }
}