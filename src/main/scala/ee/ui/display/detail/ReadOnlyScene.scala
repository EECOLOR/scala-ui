package ee.ui.display.detail

import ee.ui.implementation.contracts.SceneContract
import ee.ui.members.ReadOnlyProperty

trait ReadOnlyScene {
  def scene:ReadOnlyProperty[Option[SceneContract]]
}