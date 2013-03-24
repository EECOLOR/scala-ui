package ee.ui.display

import ee.ui.members.Property

class Window {
  private val _scene = Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value:Scene) = _scene.value = Some(value)
}