package ee.ui.display

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess

class Window {
  private val _scene = Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value: Scene) = _scene.value = Some(value)

  val showing = ReadOnlyProperty(false)
}

object Window {
  def show(window: Window) = ReadOnlyProperty.setValue(window.showing, true)(RestrictedAccess)
  def hide(window: Window) = ReadOnlyProperty.setValue(window.showing, false)(RestrictedAccess)
}