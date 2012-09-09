package ee.ui.application

import ee.ui.properties.ObservableArrayBuffer
import ee.ui.primitives.Image
import ee.ui.properties.Property
import ee.ui.properties.ObservableArrayBuffer

trait Stage extends Window {
    def primary: Boolean
    def defaultStyle: StageStyle

    def show(): Unit = writableShowing set true

    private var hasBeenVisible: Boolean = false

    override def showWindow = {
        super.showWindow
        hasBeenVisible = true
    }

    /*
	 * OWNER
	 */
    type WindowType <: Window

    val owner = new Property[Option[WindowType]](None)
    def owner_=(value: WindowType) = owner.value = Some(value)
    owner forNewValue { n =>
        if (hasBeenVisible) throw new IllegalStateException("Cannot set owner once stage has been set visible")
        if (primary) throw new IllegalStateException("Cannot set owner for the primary stage")
    }

    /*
     * STAGE STYLE
     */
    private val _style = new Property[StageStyle](defaultStyle)
    def style = _style
    def style_=(value: StageStyle) = style.value = value
    style forNewValue { n =>
        if (hasBeenVisible) throw new IllegalStateException("Cannot set style once stage has been set visible")
    }

    /*
     * MODALITY
     */

    val _modality = new Property[Modality](Modality.NONE)
    def modality = _modality
    def modality_=(value: Modality) = modality.value = value
    modality forNewValue { n =>
        if (hasBeenVisible) throw new IllegalStateException("Cannot set modality once stage has been set visible")
        if (primary) throw new IllegalStateException("Cannot set modality for the primary stage")
    }

    val _resizable = new Property(true)
    def resizable = _resizable
    def resizable_=(value: Boolean) = resizable.value = value

    val _fullScreen = new Property(false)
    def fullScreen = _fullScreen
    def fullScreen_=(value: Boolean) = fullScreen.value = value

    val _iconified = new Property(false)
    def iconified = _iconified
    def iconified_=(value: Boolean) = iconified.value = value

    val _title = new Property[Option[String]](None)
    def title = _title
    def title_=(value: String) = title.value = Some(value)

    val _minWidth = new Property(0d)
    def minWidth = _minWidth
    def minWidth_=(value: Double) = minWidth.value = value
    minWidth forNewValue { n =>
        if (n > width) width = n
    }

    val _minHeight = new Property(0d)
    def minHeight = _minHeight
    def minHeight_=(value: Double) = minHeight.value = value
    minHeight forNewValue { n =>
        if (n > height) height = n
    }

    val _maxWidth = new Property(Double.MaxValue)
    def maxWidth = _maxWidth
    def maxWidth_=(value: Double) = maxWidth.value = value
    maxWidth forNewValue { n =>
        if (n < width) width = n
    }
    
    val _maxHeight = new Property(Double.MaxValue)
    def maxHeight = _maxHeight
    def maxHeight_=(value: Double) = maxHeight.value = value
    maxHeight forNewValue { n =>
        if (n < height) height = n
    }
    
    val icons = new ObservableArrayBuffer[Image]
}

object Stage {
    val stages = new ObservableArrayBuffer[Stage]
}