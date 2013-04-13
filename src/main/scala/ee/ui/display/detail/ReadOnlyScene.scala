package ee.ui.display.detail

import ee.ui.members.ReadOnlyProperty
import ee.ui.implementation.contracts.SceneContract

trait ReadOnlyScene {
  type SceneType <: SceneContract
  def scene:ReadOnlyProperty[Option[SceneType]]
}