package ee.ui.display.detail

import ee.ui.members.ReadOnlyProperty

trait ReadOnlyScene {
  type SceneType <: ReadOnlyRoot
  def scene:ReadOnlyProperty[Option[SceneType]]
}