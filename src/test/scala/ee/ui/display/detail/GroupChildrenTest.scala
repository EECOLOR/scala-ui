package ee.ui.display.detail

import org.specs2.mutable.Specification
import ee.ui.display.Node

class GroupChildrenTest extends Specification {
  xonly
  isolated
  
  val groupChildren = new GroupChildren
  
  "GroupChildren" should {
    "exists" in {
      new GroupChildren
      ok
    }
    "be able to have a child" in {
      val node = new Node
      groupChildren(node)
      groupChildren.head === node
    }
    "be able to have more than one child" in {
      //in capitals so we can use matching
      val Node1 = new Node
      val Node2 = new Node
      groupChildren(Node1, Node2)
      groupChildren must beLike {
        case Seq(Node1, Node2) => ok
      }
    }
    "be an instance of Seq" in {
      groupChildren must beAnInstanceOf[Seq[Node]]
    }
    "should have a read only change event" in {
      //group.change must beAnInstanceOf[ReadOnlyEvent]
      todo
    }
  }
}