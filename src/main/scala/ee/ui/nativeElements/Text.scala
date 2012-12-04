package ee.ui.nativeElements

import ee.ui.traits.Position
import ee.ui.properties.Property
import ee.ui.primitives.Font
import ee.ui.primitives.VerticalPosition
import ee.ui.primitives.Paint

class Text extends Shape {
  private val _text = new Property[String]("")
  def text = _text
  def text_=(value: String) = _text.value = value

  private val _font = new Property[Font](Font.DEFAULT)
  def font = _font
  def font_=(value: Font) = _font.value = value

  private val _boundsType = new Property[TextBoundsType](TextBoundsType.LOGICAL)
  def boundsType = _boundsType
  def boundsType_=(value: TextBoundsType) = _boundsType.value = value

  private val _textOrigin = new Property[VerticalPosition](VerticalPosition.TOP)
  def textOrigin = _textOrigin
  def textOrigin_=(value: VerticalPosition) = _textOrigin.value = value

  private val _wrappingWidth = new Property(0d)
  def wrappingWidth = _wrappingWidth
  def wrappingWidth_=(value: Double) = _wrappingWidth.value = value

  private val _underline = new Property[Boolean](false)
  def underline = _underline
  def underline_=(value: Boolean) = _underline.value = value

  private val _strikethrough = new Property[Boolean](false)
  def strikethrough = _strikethrough
  def strikethrough_=(value: Boolean) = _strikethrough.value = value

  private val _textAlignment = new Property[TextAlignment](TextAlignment.LEFT)
  def textAlignment = _textAlignment
  def textAlignment_=(value: TextAlignment) = _textAlignment.value = value

  private val _fontSmoothingType = new Property[FontSmoothingType](FontSmoothingType.GRAY)
  def fontSmoothingType = _fontSmoothingType
  def fontSmoothingType_=(value: FontSmoothingType) = _fontSmoothingType.value = value
}

sealed abstract class TextBoundsType

object TextBoundsType {

  /**
   * Use logical bounds as the basis for calculating the bounds.
   * <p>
   * Note: This is usually the fastest option.
   */
  object LOGICAL extends TextBoundsType

  /**
   * Use visual bounds as the basis for calculating the bounds.
   * <p>
   * Note: This is likely to be slower than using logical bounds.
   */
  object VISUAL extends TextBoundsType

}

sealed abstract class TextAlignment

object TextAlignment {

  /**
   * Represents text alignment to the left (left-justified, ragged right).
   */
  object LEFT extends TextAlignment

  /**
   * Represents centered text alignment (ragged left and right).
   */
  object CENTER extends TextAlignment

  /**
   * Represents text alignment to the right (right-justified, ragged left).
   */
  object RIGHT extends TextAlignment

  /**
   * Represents justified text alignment.
   */
  object JUSTIFY extends TextAlignment

}

sealed abstract class FontSmoothingType

object FontSmoothingType {

  /**
   * Specifies the default gray scale smoothing, which is most
   * suitable for graphics and animation uses.
   */
  object GRAY extends FontSmoothingType

  /**
   * Specifies sub-pixel LCD text, which utilises characteristics
   * of digital LCD display panels to achieve increased pixel
   * resolution. This mode is generally appropriate where the
   * important factor is legibility of static text, particularly
   * at small sizes.
   * <p>
   * A request for LCD text is a 'hint', since the implementation
   * may need to ignore it under conditions such as compositing modes
   * which do not support it. It follow that where LCD text is important
   * that the application should avoid use of effects, transparency etc.
   */
  object LCD extends FontSmoothingType
}