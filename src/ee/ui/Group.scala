package ee.ui

import ee.ui.properties.ObservableArrayBuffer
import scala.collection.mutable.ListBuffer
import ee.ui.properties.Change
import ee.ui.properties.Add
import ee.ui.properties.Clear
import ee.ui.properties.Remove

class Group extends Node {

  val children: Children = new Children

  class Children extends ObservableArrayBuffer[Node] {

    val removed = ListBuffer[Change[Node]]()
    val added = ListBuffer[Change[Node]]()

    def reset = {
      removed.clear
      added.clear
    }

    onChangedIn {
      case x: Add[_] => added += x
      case x: Remove[_] => removed += x
      case Clear(elements) => removed ++= elements.view.zipWithIndex.map {
        case (elem, index) => Remove(index, elem)
      }
    }
  }

}

