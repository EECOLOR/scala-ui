package ee.ui.display.detail

import org.specs2.mutable.Specification
import ee.ui.display.Node
import utils.TestUtils
import ee.ui.members.ReadOnlyEvent
import utils.TypeTest
import scala.collection.mutable.ListBuffer
import ee.ui.events.Change
import ee.ui.events.Add
import ee.ui.events.Clear
import ee.ui.events.Remove
import ee.ui.events.Clear
import ee.ui.members.ObservableArrayBuffer

class GroupChildrenTest extends Specification {
  xonly
  isolated

  val groupChildren = new GroupChildren

  val resultingEvents = ListBuffer.empty[Change[Node]]
  def n = new Node

  "GroupChildren" should {
    "be able to have a child" in {
      val node = n
      groupChildren(node)
      groupChildren.head === node
    }
    "be able to have more than one child" in {
      //in capitals so we can use matching
      val Node1 = n
      val Node2 = n
      groupChildren(Node1, Node2)
      groupChildren must beLike {
        case Seq(Node1, Node2) => ok
      }
    }
    "be an instance of ObservableArrayBuffer" in {
      groupChildren must beAnInstanceOf[ObservableArrayBuffer[Node]]
    }
  }
}