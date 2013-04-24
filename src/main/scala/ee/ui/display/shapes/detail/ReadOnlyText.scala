package ee.ui.display.shapes.detail

import ee.ui.display.detail.ReadOnlyShape
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.primitives.VerticalPosition

trait ReadOnlyText extends ReadOnlyShape {
  protected val _text = Property("")
  def text: ReadOnlyProperty[String] = _text
  
  protected val _textAlignment = Property[TextAlignment](TextAlignment.LEFT)
  def textAlignment: ReadOnlyProperty[TextAlignment] = _textAlignment
  
  protected val _textOrigin = Property[VerticalPosition](VerticalPosition.TOP)
  def textOrigin: ReadOnlyProperty[VerticalPosition] = _textOrigin
  
  protected val _wrappingWidth = Property[Double](0)
  def wrappingWidth: ReadOnlyProperty[Double] = _wrappingWidth
}