package ee.ui.primitives.transformation

import ee.ui.primitives.Transformation
import ee.ui.primitives.Point
import ee.ui.primitives.Bounds

object Identity extends Transformation {
  override lazy val isPureTranslation = true
  override lazy val isIdentity = true

  override def transform(p: Point) = p
  override def transform(b: Bounds) = b
  
  override def ++(t: Transformation) = t
}