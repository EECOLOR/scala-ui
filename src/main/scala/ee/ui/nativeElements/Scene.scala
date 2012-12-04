package ee.ui.nativeElements

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.Group
import ee.ui.primitives.Color
import ee.ui.primitives.Camera
import ee.ui.events.Event
import ee.ui.events.MouseEvent
import ee.ui.events.NullEvent
import ee.ui.traits.Fill
import ee.ui.Node
import ee.ui.primitives.Point
import ee.ui.properties.Binding._
import ee.ui.events.NullEvent
import ee.ui.events.MouseEvent
import ee.ui.events.MouseButton
import ee.ui.events.CharacterTypedEvent
import ee.ui.events.KeyEvent
import ee.ui.traits.KeyBindings
import ee.ui.traits.KeyEvents
import ee.ui.nativeElements.scene.MouseHandling
import ee.ui.nativeElements.scene.KeyHandling
import ee.ui.nativeElements.scene.FocusHandling
import ee.ui.traits.Position
import ee.ui.traits.Size
import ee.ui.traits.ReadOnlySize

class Scene(defaultDepthBuffer: Boolean = false) extends Position with ReadOnlySize
  with Fill with MouseHandling with FocusHandling with KeyHandling with KeyEvents {

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

