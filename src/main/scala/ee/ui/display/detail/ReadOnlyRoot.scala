package ee.ui.display.detail
import ee.ui.display.traits.ReadOnlySize
import ee.ui.members.ReadOnlyProperty
import ee.ui.display.implementation.contracts.NodeContract

trait ReadOnlyRoot {
  def root:ReadOnlyProperty[Option[NodeContract]]
}