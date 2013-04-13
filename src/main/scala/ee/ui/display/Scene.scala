package ee.ui.display

import ee.ui.members.Property
import ee.ui.display.detail.ReadOnlyRoot
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.detail.ReadOnlyNode
import ee.ui.implementation.contracts.SceneContract
import ee.ui.display.implementation.contracts.NodeContract

class Scene extends SceneContract {
  val _root = Property[Option[NodeContract]](None)
  def root = _root
  def root_=(value: NodeContract) = _root.value = Some(value)
  def root_=(value: Option[NodeContract]) = _root.value = value
}