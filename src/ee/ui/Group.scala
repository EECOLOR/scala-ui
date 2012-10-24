package ee.ui

import ee.ui.properties.ObservableArrayBuffer
import scala.collection.mutable.ListBuffer
import ee.ui.properties.Change
import ee.ui.properties.Add
import ee.ui.properties.Clear
import ee.ui.properties.Remove
import ee.ui.traits.RestrictedAccess
import ee.ui.layout.CanResizeToChildren

class Group extends Node with CanResizeToChildren {

  val children: Children = new Children

  class Children extends ObservableArrayBuffer[Node] {

    def apply(nodes: Node*): Unit =
      this ++= nodes

    val removed = ListBuffer[Remove[Node]]()
    val added = ListBuffer[Add[Node]]()

    def reset = {
      removed.clear
      added.clear
    }

    onChangedIn {
      case x @ Add(index, element) => {
        //remove the node from the parent
        element.parent foreach (_.children -= element)
        //set the parent to this group
        Node setParent (element, Some(Group.this))
        //remember it was added
        added += x
      }
      case x @ Remove(index, element) => {
        //on removal set the parent to None
        Node setParent (element, None)
        //remember it was removed
        removed += x
      }
      case Clear(elements) => removed ++= elements.view.zipWithIndex.map {
        case (elem, index) => Remove(index, elem)
      }
    }
  }

}

