package ee.ui.display

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess

class Window {
  private val _scene = Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value: Scene) = _scene.value = Some(value)
  def scene_=(value: Option[Scene]) = _scene.value = value

  val showing = ReadOnlyProperty(false)

  val _title = Property[Option[String]](None)
  def title = _title
  def title_=(value: String) = _title.value = Some(value)
  def title_=(value: Option[String]) = _title.value = value

}

object Window {
  def show(window: Window) = ReadOnlyProperty.setValue(window.showing, true)(RestrictedAccess)
  def hide(window: Window) = ReadOnlyProperty.setValue(window.showing, false)(RestrictedAccess)
}