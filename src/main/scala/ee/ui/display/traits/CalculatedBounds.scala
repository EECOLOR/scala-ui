package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.primitives.Bounds

trait CalculatedBounds extends CalculatedTransformation {

  protected val untransformedBounds = Property(Bounds.ZERO)

  untransformedBounds <== width | height map {
    case (width, height) => Bounds(0, 0, width, height)
  }

  protected val _bounds = Property(Bounds.ZERO)
  val bounds: ReadOnlyProperty[Bounds] = _bounds

  _bounds <== (untransformedBounds | totalTransformation) map {
    case (untransformedBounds, totalTransformation) => totalTransformation transform untransformedBounds
  }

}