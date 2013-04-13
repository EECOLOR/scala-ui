package ee.ui.display

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess
import ee.ui.display.traits.Size
import ee.ui.display.traits.Title
import ee.ui.display.detail.ReadOnlyScene
import ee.ui.implementation.contracts.SceneContract
import ee.ui.system.AccessRestriction

class Window extends Size with Title with ReadOnlyScene {
  
  private val _scene = Property[Option[SceneContract]](None)
  def scene = _scene
  def scene_=(value: SceneContract) = _scene.value = Some(value)
  def scene_=(value: Option[SceneContract]) = _scene.value = value

  val showing = ReadOnlyProperty(false)

}

object Window {
  def show(window: Window)(implicit ev:AccessRestriction) = ReadOnlyProperty.setValue(window.showing, true)
  def hide(window: Window)(implicit ev:AccessRestriction) = ReadOnlyProperty.setValue(window.showing, false)
}