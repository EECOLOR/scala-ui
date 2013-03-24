package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.Property

class NodeTest extends Specification {

  xonly
  isolated

  val node = new Node
  
  "Node" should {
    "have a width with a default value of 0" in {
      node.width.value === 0
    }
    "have a height with a default value of 0" in {
      node.height.value === 0
    }
  }
}