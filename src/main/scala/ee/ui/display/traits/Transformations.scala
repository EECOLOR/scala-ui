package ee.ui.display.traits

import ee.ui.members.ObservableArrayBuffer
import ee.ui.members.ObservableSeq
import ee.ui.primitives.Transformation

trait ReadOnlyTransformations {

  protected val _transformations = ObservableArrayBuffer.empty[Transformation]
  def transformations:ObservableSeq[Transformation] = _transformations
  
}

trait Transformations extends ReadOnlyTransformations {
  override def transformations = _transformations
}