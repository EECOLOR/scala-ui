package ee.ui.display.detail
import ee.ui.display.traits.ReadOnlySize
import ee.ui.members.ReadOnlyProperty

trait ReadOnlyRoot {
  def root:ReadOnlyProperty[Option[NodeContract]]
}