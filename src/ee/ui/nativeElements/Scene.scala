package ee.ui.nativeElements

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.Group
import ee.ui.primitives.Color
import ee.ui.primitives.Camera
import ee.ui.events.Event
import ee.ui.events.MouseEvent

class Scene(defaultDepthBuffer:Boolean = false) extends NativeElement[Scene] {
	
  def nativeElement = createNativeElement
  
  lazy val depthBuffer:ReadOnlyProperty[Boolean] = new Property(defaultDepthBuffer)
  
  private val _root = new Property[Option[Group]](None)
  def root = _root
  def root_=(value:Group) = root.value = Some(value)
  
  private val _fill = new Property[Color](Color.WHITE)
  def fill = _fill
  def fill_=(value:Color) = fill.value = value
  
  private val _camera = new Property[Option[Camera]](None)
  def camera = _camera
  def camera_=(value:Camera) = camera.value = Some(value)
  
  val onMouseClicked = new Event[MouseEvent]
}