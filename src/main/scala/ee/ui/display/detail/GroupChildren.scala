package ee.ui.display.detail

import ee.ui.display.Node
import scala.collection.mutable.ArrayBuffer

class GroupChildren extends ArrayBuffer[Node] {
  def apply(nodes: Node*) = 
    this ++= nodes
}