package ee.ui.display

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess
import ee.ui.display.traits.Size
import ee.ui.display.traits.Title
import ee.ui.display.detail.ReadOnlyScene

class Window extends Size with Title with ReadOnlyScene {
  type SceneType = Scene
  
  private val _scene = Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value: Scene) = _scene.value = Some(value)
  def scene_=(value: Option[Scene]) = _scene.value = value

  val showing = ReadOnlyProperty(false)

}

object Window {
  def show(window: Window) = ReadOnlyProperty.setValue(window.showing, true)(RestrictedAccess)
  def hide(window: Window) = ReadOnlyProperty.setValue(window.showing, false)(RestrictedAccess)
}