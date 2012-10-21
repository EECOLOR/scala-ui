package ee.ui.nativeElements

import ee.ui.Node
import ee.ui.properties.Property
import ee.ui.primitives.Paint
import ee.ui.primitives.Color
import ee.ui.traits.Fill

abstract class Shape extends Node with Fill {

  def defaultFill = Color.BLACK
  
  private val _antialiased = new Property[Boolean](true)
  def antialiased = _antialiased
  def antialiased_=(value:Boolean) = _antialiased.value = value
  
  private val _stroke = new Property[Option[Paint]](None)
  def stroke = _stroke
  def stroke_=(value:Paint) = _stroke.value = Some(value)
  def stroke_=(value:Option[Paint]) = _stroke.value = value
  
  private val _strokeWidth = new Property(0f)
  def strokeWidth = _strokeWidth
  def strokeWidth_=(value:Float) = _strokeWidth.value = value
  
  private val _strokeType = new Property[StrokeType](StrokeType.CENTERED)
  def strokeType = _strokeType
  def strokeType_=(value:StrokeType) = _strokeType.value = value
  
  private val _strokeLineCap = new Property[StrokeLineCap](StrokeLineCap.SQUARE)
  def strokeLineCap = _strokeLineCap
  def strokeLineCap_=(value:StrokeLineCap) = _strokeLineCap.value = value
  
  private val _strokeLineJoin = new Property[StrokeLineJoin](StrokeLineJoin.MITER)
  def strokeLineJoin = _strokeLineJoin
  def strokeLineJoin_=(value:StrokeLineJoin) = _strokeLineJoin.value = value
  
}

sealed abstract class StrokeType

object StrokeType {

    object INSIDE extends StrokeType
    object OUTSIDE extends StrokeType
    object CENTERED extends StrokeType
}

sealed abstract class StrokeLineCap 

object StrokeLineCap {

    object SQUARE extends StrokeLineCap
    object BUTT extends StrokeLineCap
    object ROUND extends StrokeLineCap
}

sealed abstract class StrokeLineJoin

object StrokeLineJoin {

    object MITER extends StrokeLineJoin
    object BEVEL extends StrokeLineJoin
    object ROUND extends StrokeLineJoin
}
