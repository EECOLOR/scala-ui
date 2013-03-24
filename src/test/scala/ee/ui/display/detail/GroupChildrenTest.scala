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
    "be an instance of Seq" in {
      groupChildren must beAnInstanceOf[Seq[Node]]
    }
    "have a read only change event" in {
      TypeTest[ReadOnlyEvent[Change[Node]]].forInstance(groupChildren.change)
    }
    "dispatch an event when" in {
      "one node is added" in {
        val node = n
        groupChildren.change { resultingEvents += _ }
        groupChildren += node

        resultingEvents.toSeq === Seq(Add(0, node))
      }
      "multiple nodes are added" in {
        val nodes = Seq(n, n)
        groupChildren.change { resultingEvents += _ }
        groupChildren ++= nodes

        resultingEvents.toSeq === Seq(Add(0, nodes(0)), Add(1, nodes(1)))
      }
      "a node is added using a right associative method" in {
        val node = n
        groupChildren.change { resultingEvents += _ }
        node +=: groupChildren

        resultingEvents.toSeq === Seq(Add(0, node))
      }
      "a node is updated" in {
        val node1 = n
        val node2 = n
        groupChildren += node1
        groupChildren.change { resultingEvents += _ }
        groupChildren.update(0, node2)

        resultingEvents.toSeq === Seq(Remove(0, node1), Add(0, node2))
      }
      "a node is removed by index" in {
        val node = n
        groupChildren += node
        groupChildren.change { resultingEvents += _ }
        groupChildren.remove(0)

        resultingEvents.toSeq === Seq(Remove(0, node))
      }
      "a node is removed" in {
        val node = n
        groupChildren += node
        groupChildren.change { resultingEvents += _ }
        groupChildren -= node

        resultingEvents.toSeq === Seq(Remove(0, node))
      }
      "the group is cleared" in {
        val node = n
        groupChildren += node
        groupChildren.change { resultingEvents += _ }
        groupChildren.clear

        resultingEvents.toSeq === Seq(Clear(Seq(node)))
      }
      "inserted a collection at a given index" in {
        val node1 = n
        val node2 = n
        groupChildren ++= Seq(n, n)
        groupChildren.change { resultingEvents += _ }
        groupChildren.insertAll(1, Seq(node1, node2))
        
        resultingEvents.toSeq === Seq(Add(1, node1), Add(2, node2))
      }
    }
  }
}