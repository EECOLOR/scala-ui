package ee.ui.display

import org.specs2.mutable.Specification

class GroupTest extends Specification {
  xonly
  isolated

  val group = new Group
  
  "Group" should {
    "extend node" in {
      group must beAnInstanceOf[Node]
    }
    "be able to have a child" in {
      group.children(new Node)
      ok
    }
    "be able to have more than one child" in {
      group.children(new Node, new Node)
      ok
    }
  }
}