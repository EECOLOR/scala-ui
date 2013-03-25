package ee.ui.display.detail

import ee.ui.display.Node
import scala.collection.mutable.ArrayBuffer
import ee.ui.members.ReadOnlyEvent
import ee.ui.events.Change
import scala.collection.mutable.Buffer
import ee.ui.system.RestrictedAccess
import ee.ui.events.Add
import ee.ui.events.Remove
import ee.ui.events.Clear
import ee.ui.members.ObservableArrayBuffer

class GroupChildren extends ObservableArrayBuffer[Node] {
  def apply(nodes: Node*) =
    this ++= nodes

}