package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.display.detail.GroupChildren

class GroupTest extends Specification {
  xonly
  isolated

  val group = new Group
  
  "Group" should {
    "extend node" in {
      group must beAnInstanceOf[Node]
    }
    
    "have a collection of children" in {
      group.children must beAnInstanceOf[GroupChildren]
    }
  }
}