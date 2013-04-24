package ee.ui.display.shapes

import ee.ui.display.implementation.contracts.TextContract
import ee.ui.display.Node
import ee.ui.display.shapes.detail.TextAlignment
import ee.ui.primitives.VerticalPosition

class Text extends Node with TextContract {
  
  override def text = _text
  def text_=(value: String) = _text.value = value
  
  override def textAlignment = _textAlignment
  def textAlignment_=(value: TextAlignment) = _textAlignment.value = value
  
  override def textOrigin = _textOrigin
  def textOrigin_=(value: VerticalPosition) = _textOrigin.value = value
  
  override def wrappingWidth = _wrappingWidth
  def wrappingWidth_=(value: Double) = _wrappingWidth.value = value
  
}