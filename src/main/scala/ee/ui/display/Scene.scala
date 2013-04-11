package ee.ui.display

import ee.ui.members.Property
import ee.ui.display.detail.ReadOnlyRoot
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.detail.NodeContract
import ee.ui.display.detail.ReadOnlyNode

class Scene extends ReadOnlyRoot {
  val _root = Property[Option[NodeContract]](None)
  def root = _root
  def root_=(value: NodeContract) = _root.value = Some(value)
  def root_=(value: Option[NodeContract]) = _root.value = value

}