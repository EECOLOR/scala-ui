package ee.ui.nativeElements

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.traits.Size
import ee.ui.traits.Position
import ee.ui.traits.Focus
import scala.collection.mutable.ListBuffer
import ee.ui.nativeImplementation.ElementImplementationHandler
import ee.ui.traits.OnCreate
import ee.ui.application.ImplicitNativeManager
import ee.ui.events.NullEvent
import ee.ui.application.ImplicitNativeManager
import ee.ui.properties.ObservableArrayBuffer
import ee.ui.primitives.Image
import ee.ui.traits.ReadOnlyPosition
import ee.ui.traits.ReadOnlySize
import ee.ui.traits.ReadOnlyFocus

class Window(val primary: Boolean = false, val defaultStyle: WindowStyle = WindowStyle.DECORATED) 
	extends ReadOnlyPosition with ReadOnlySize with ReadOnlyFocus {

  private val writableShowing = new Property(false)
  lazy val showing: ReadOnlyProperty[Boolean] = writableShowing

  private val _opacity = new Property(1.0)
  def opacity = _opacity
  def opacity_=(value: Double) = opacity.value = value

  private val _scene = new Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value: Scene) = scene.value = Some(value)

  private var hasBeenVisible: Boolean = false

  showing forNewValueIn {
    case true => hasBeenVisible = true
  }

  /*
	 * OWNER
	 */

  private val _owner = new Property[Option[Window]](None)
  def owner = _owner
  def owner_=(value: Window) = owner.value = Some(value)
  owner forNewValue { n =>
    if (hasBeenVisible) throw new IllegalStateException("Cannot set owner once stage has been set visible")
    if (primary) throw new IllegalStateException("Cannot set owner for the primary stage")
  }

  /*
     * STAGE STYLE
     */
  private val _style = new Property(defaultStyle)
  def style = _style
  def style_=(value: WindowStyle) = style.value = value
  style forNewValue { n =>
    if (hasBeenVisible) throw new IllegalStateException("Cannot set style once stage has been set visible")
  }

  /*
     * MODALITY
     */

  private val _modality = new Property[Modality](Modality.NONE)
  def modality = _modality
  def modality_=(value: Modality) = modality.value = value
  modality forNewValue { n =>
    if (hasBeenVisible) throw new IllegalStateException("Cannot set modality once stage has been set visible")
    if (primary) throw new IllegalStateException("Cannot set modality for the primary stage")
  }

  private val _resizable = new Property(true)
  def resizable = _resizable
  def resizable_=(value: Boolean) = resizable.value = value

  private val _fullScreen = new Property(false)
  def fullScreen = _fullScreen
  def fullScreen_=(value: Boolean) = fullScreen.value = value

  private val _iconified = new Property(false)
  def iconified = _iconified
  def iconified_=(value: Boolean) = iconified.value = value

  private val _title = new Property[Option[String]](None)
  def title = _title
  def title_=(value: String) = title.value = Some(value)

  private val _minWidth = new Property(0d)
  def minWidth = _minWidth
  def minWidth_=(value: Double) = minWidth.value = value
  //TODO move this to implementation
  minWidth forNewValue { n =>
    //if (n > width) width = n
  }

  private val _minHeight = new Property(0d)
  def minHeight = _minHeight
  def minHeight_=(value: Double) = minHeight.value = value
  //TODO move this to implementation
  minHeight forNewValue { n =>
    //if (n > height) height = n
  }

  private val _maxWidth = new Property(Double.MaxValue)
  def maxWidth = _maxWidth
  def maxWidth_=(value: Double) = maxWidth.value = value
  ////TODO move this to implementation
  maxWidth forNewValue { n =>
    //if (n < width) width = n
  }

  private val _maxHeight = new Property(Double.MaxValue)
  def maxHeight = _maxHeight
  def maxHeight_=(value: Double) = maxHeight.value = value
  //TODO move this to implementation
  maxHeight forNewValue { n =>
    //if (n < height) height = n
  }

  val icons = new ObservableArrayBuffer[Image]

}

object Window extends ImplicitNativeManager {
  private val _windows = ListBuffer[Window]()

  def windows = _windows.toSeq

  def show(window: Window): Unit = {
    implicitly[ElementImplementationHandler] windowCreated window
    _windows += window
    window.writableShowing.value = true
  }

  def hide(window: Window): Unit = {
    window.writableShowing.value = false
    _windows -= window
  }
}

sealed abstract class Modality

object Modality {

  object NONE extends Modality
  object WINDOW_MODAL extends Modality
  object APPLICATION_MODAL extends Modality
}

sealed abstract class WindowStyle()

object WindowStyle {

  object DECORATED extends WindowStyle
  object UNDECORATED extends WindowStyle
  object TRANSPARENT extends WindowStyle
  object UTILITY extends WindowStyle

}