package ee.ui.display.detail

import ee.ui.display.Node
import ee.ui.members.ObservableArrayBuffer

class GroupChildren extends ObservableArrayBuffer[Node] {
  def apply(nodes: Node*) =
    this ++= nodes

}