package ee.ui.display

import ee.ui.display.implementation.contracts.NodeContract
import ee.ui.implementation.contracts.SceneContract
import ee.ui.members.Property

class Scene extends SceneContract {
  val _root = Property[Option[NodeContract]](None)
  def root = _root
  def root_=(value: NodeContract) = _root.value = Some(value)
  def root_=(value: Option[NodeContract]) = _root.value = value
}