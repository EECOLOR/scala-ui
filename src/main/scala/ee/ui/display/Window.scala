package ee.ui.display

import ee.ui.members.ReadOnlyProperty
import ee.ui.display.traits.Size
import ee.ui.display.traits.Focus
import scala.collection.mutable.ListBuffer
import ee.ui.events.NullEvent
import ee.ui.primitives.Image
import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.traits.ReadOnlyFocus
import ee.ui.display.traits.ReadOnlyPosition
import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.members.ObservableArrayBuffer
import ee.ui.members.Property
import ee.ui.system.AccessRestriction
import ee.ui.display.traits.Position
import ee.ui.display.traits.ExplicitSize
import ee.ui.display.traits.ExplicitPosition

class Window(val primary: Boolean = false, val defaultStyle: WindowStyle = WindowStyle.DECORATED)
  extends ExplicitPosition with ExplicitSize with ReadOnlyFocus {

  private val writableShowing = new Property(false)
  val showing: ReadOnlyProperty[Boolean] = writableShowing

  private val _opacity = new Property(1.0)
  def opacity = _opacity
  def opacity_=(value: Double) = opacity.value = value

  private val _scene = new Property[Option[Scene]](None)
  def scene = _scene
  def scene_=(value: Scene) = scene.value = Some(value)

  private val hasBeenVisible = Property(false)
  showing.change.once apply { 
    hasBeenVisible.value = true
  }

  /*
	 * OWNER
	 */

  private val _owner = new Property[Option[Window]](None)
  def owner = _owner
  def owner_=(value: Window) = owner.value = Some(value)
  owner.change {
    if (hasBeenVisible) throw new IllegalStateException("Cannot set owner once stage has been set visible")
    if (primary) throw new IllegalStateException("Cannot set owner for the primary stage")
  }

  /*
     * STAGE STYLE
     */
  private val _style = new Property(defaultStyle)
  def style = _style
  def style_=(value: WindowStyle) = style.value = value
  style.change {
    if (hasBeenVisible) throw new IllegalStateException("Cannot set style once stage has been set visible")
  }

  /*
     * MODALITY
     */

  private val _modality = new Property[Modality](Modality.NONE)
  def modality = _modality
  def modality_=(value: Modality) = modality.value = value
  modality.change {
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
  width <== minWidth filter (_ > width)

  private val _minHeight = new Property(0d)
  def minHeight = _minHeight
  def minHeight_=(value: Double) = minHeight.value = value
  height <== minHeight filter (_ > height)

  private val _maxWidth = new Property(Double.MaxValue)
  def maxWidth = _maxWidth
  def maxWidth_=(value: Double) = maxWidth.value = value
  width <== maxWidth filter (_ < width)
  height <== maxHeight filter (_ < height)

  private val _maxHeight = new Property(Double.MaxValue)
  def maxHeight = _maxHeight
  def maxHeight_=(value: Double) = maxHeight.value = value
  height <== maxHeight filter (_ < height)

  val icons = new ObservableArrayBuffer[Image]

}

object Window {

  def show(window: Window)(implicit ev: AccessRestriction): Unit =
    window.writableShowing.value = true

  def hide(window: Window)(implicit ev: AccessRestriction): Unit =
    window.writableShowing.value = false
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