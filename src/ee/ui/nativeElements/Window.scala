package ee.ui.nativeElements

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.traits.Size
import ee.ui.traits.Position
import ee.ui.traits.Focus
import scala.collection.mutable.ListBuffer
import ee.ui.application.ImplicitApplicationDependencies
import ee.ui.nativeImplementation.NativeManager
import ee.ui.traits.OnCreate

abstract class Window extends OnCreate with ImplicitApplicationDependencies with Position with Size with Focus {

  def onCreate = implicitly[NativeManager] windowCreated this
  
  protected val writableShowing = new Property(false)
  lazy val showing: ReadOnlyProperty[Boolean] = writableShowing

  showing forNewValue { show =>
    if (show) {
      Window.windows += this
    } else
      Window.windows -= this
  }

  private val _opacity = new Property(1.0)
  def opacity = _opacity
  def opacity_=(value: Double) = opacity.value = value

  private val _scene = new Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value: Scene) = scene.value = Some(value)
}

object Window {
  val windows = ListBuffer[Window]()
}