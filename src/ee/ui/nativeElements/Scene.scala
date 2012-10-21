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

class Scene(defaultDepthBuffer:Boolean = false) extends Fill {
	
  def defaultFill = Color.WHITE
  
  lazy val depthBuffer:ReadOnlyProperty[Boolean] = new Property(defaultDepthBuffer)
  
  private val _root = new Property[Option[Group]](None)
  def root = _root
  def root_=(value:Group) = root.value = Some(value)
  def root_=(value:Option[Group]) = root.value = value
  
  private val _camera = new Property[Option[Camera]](None)
  def camera = _camera
  def camera_=(value:Camera) = camera.value = Some(value)
  def camera_=(value:Option[Camera]) = camera.value = value
  
  val onMouseClicked = new Event[MouseEvent]
  val pulse = new NullEvent
  
}