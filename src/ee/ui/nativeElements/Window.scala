package ee.ui.nativeElements

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.traits.Size
import ee.ui.traits.Position
import ee.ui.traits.Focus
import scala.collection.mutable.ListBuffer
import ee.ui.nativeImplementation.NativeManager
import ee.ui.traits.OnCreate
import ee.ui.application.ImplicitNativeManager
import ee.ui.events.NullEvent
import ee.ui.application.ImplicitNativeManager

abstract class Window extends Position with Size with Focus {

  private val writableShowing = new Property(false)
  lazy val showing: ReadOnlyProperty[Boolean] = writableShowing

  private val _opacity = new Property(1.0)
  def opacity = _opacity
  def opacity_=(value: Double) = opacity.value = value

  private val _scene = new Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value: Scene) = scene.value = Some(value)

}

object Window extends ImplicitNativeManager {
  private val _windows = ListBuffer[Window]()

  def windows = _windows.toSeq
  
  def show(window: Window): Unit = {
    implicitly[NativeManager] windowCreated window
    _windows += window
    window.writableShowing.value = true
  }

  def hide(window: Window): Unit = {
    window.writableShowing.value = false
    _windows -= window
  }
}