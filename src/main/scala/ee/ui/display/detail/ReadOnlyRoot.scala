package ee.ui.display.detail

import ee.ui.display.implementation.contracts.NodeContract
import ee.ui.members.ReadOnlyProperty

trait ReadOnlyRoot {
  def root:ReadOnlyProperty[Option[NodeContract]]
}