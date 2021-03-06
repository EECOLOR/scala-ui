package ee.ui.display

import ee.ui.display.scene.FocusHandling
import ee.ui.display.scene.KeyHandling
import ee.ui.display.scene.MouseHandling
import ee.ui.display.traits.Fill
import ee.ui.display.traits.KeyEvents
import ee.ui.display.traits.Position
import ee.ui.display.traits.ReadOnlySize
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.primitives.Camera
import ee.ui.primitives.Color

class Scene(defaultDepthBuffer: Boolean = false) extends Position with ReadOnlySize
  with Fill with MouseHandling with FocusHandling with KeyEvents with KeyHandling {

  def defaultFill = Color.WHITE

  lazy val depthBuffer: ReadOnlyProperty[Boolean] = new Property(defaultDepthBuffer)

  private val _root = new Property[Option[Node]](None)
  def root = _root
  def root_=(value: Node) = root.value = Some(value)
  def root_=(value: Option[Node]) = root.value = value

  private val _camera = new Property[Option[Camera]](None)
  def camera = _camera
  def camera_=(value: Camera) = camera.value = Some(value)
  def camera_=(value: Option[Camera]) = camera.value = value

}

