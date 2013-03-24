package ee.ui.display

import ee.ui.members.Property

class Scene {
  val _root = Property[Option[Node]](None)
  def root = _root
  def root_=(value: Node) = _root.value = Some(value)

}