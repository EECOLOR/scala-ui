package ee.ui.display

import ee.ui.properties.ObservableArrayBuffer
import ee.ui.properties.Add
import ee.ui.properties.Clear
import ee.ui.properties.Remove
import ee.ui.properties.Remove.apply
import scala.collection.mutable.ListBuffer
import ee.ui.system.RestrictedAccess

class Group extends Node {

  val children: Children = new Children

  class Children extends ObservableArrayBuffer[Node] {

    def apply(nodes: Node*): Unit =
      this ++= nodes

    private def elementAdded(element:Node) = {
        //remove the node from the previous parent
        element.parent foreach (_.children -= element)
        //set the parent to this group
        Node.setParent(element, Some(Group.this))(RestrictedAccess)
    }
      
    private def elementRemoved(element:Node) = 
        //on removal set the parent to None
        Node.setParent(element, None)(RestrictedAccess)
    
    change collect {
      case Add(index, element) => elementAdded(element)
      case Remove(index, element) => elementRemoved(element)
      case Clear(elements) => elements foreach elementRemoved
    }
  }

}

