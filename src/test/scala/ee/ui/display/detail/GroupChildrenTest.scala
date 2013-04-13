package ee.ui.display.detail

import scala.collection.mutable.ListBuffer
import org.specs2.mutable.Specification
import ee.ui.display.Node
import ee.ui.events.Change
import ee.ui.members.ObservableArrayBuffer
import utils.SubtypeTest

class GroupChildrenTest extends Specification {
  
  xonly
  isolated

  val groupChildren = new GroupChildren
  def createNode = new Node {}
  
  "GroupChildren" should {

    "extend the correct types" in {
      SubtypeTest[GroupChildren <:< ObservableArrayBuffer[Node]]
    }
    
    "be able to have a child" in {
      val node = createNode
      groupChildren(node)
      groupChildren.head === node
    }
    
    "be able to have more than one child" in {
      val node1 = createNode
      val node2 = createNode
      groupChildren(node1, node2)
      groupChildren.toSeq === Seq(node1, node2)
    }
  }
}