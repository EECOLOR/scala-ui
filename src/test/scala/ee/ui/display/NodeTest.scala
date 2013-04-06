package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.Property
import ee.ui.display.traits.ReadOnlySize

class NodeTest extends Specification {

  xonly
  isolated

  val node = new Node
  
  "Node" should {
    "have a readonly size" in {
      node must beAnInstanceOf[ReadOnlySize]
    }
  }
}