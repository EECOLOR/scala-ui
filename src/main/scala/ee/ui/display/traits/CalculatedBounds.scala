package ee.ui.display.traits

import ee.ui.primitives.Bounds
import ee.ui.primitives.Bounds.apply
import ee.ui.primitives.Identity
import ee.ui.primitives.Point.apply
import ee.ui.primitives.Rotate.apply
import ee.ui.primitives.Scale.apply
import ee.ui.primitives.Transformation
import ee.ui.primitives.Translate.apply
import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.primitives.Point
import ee.ui.primitives.Rotate
import ee.ui.primitives.Scale
import ee.ui.primitives.Translate
import ee.ui.bindings.BindingFactory
import ee.ui.observables.ObservableValue
import ee.ui.properties.Variable

trait CalculatedBounds extends Position with Size
  with Translation with Scaling with Rotation with Transformations {

  private val writableShapeTransformation = new Property[Transformation](Identity)
  val shapeTransformation: ReadOnlyProperty[Transformation] = writableShapeTransformation

  private val writablePositionTransformation = new Property[Transformation](Identity)
  val positionTransformation: ReadOnlyProperty[Transformation] = writablePositionTransformation

  private val writableTotalTransformation = new Property[Transformation](Identity)
  val totalTransformation: ReadOnlyProperty[Transformation] = writableTotalTransformation

  private val writableUntransformedBounds = new Property(Bounds.ZERO)
  val untransformedBounds: ReadOnlyProperty[Bounds] = writableUntransformedBounds

  private val writableBounds = new Property(Bounds.ZERO)
  val bounds: ReadOnlyProperty[Bounds] = writableBounds

  private val writableShapeBounds = new Property(Bounds.ZERO)
  val shapeBounds: ReadOnlyProperty[Bounds] = writableShapeBounds

  writableTotalTransformation <== shapeTransformation | positionTransformation map {
    case (shapeTransformation, positionTransformation) => {
      val propertyDerivedTransformation: Transformation =
        positionTransformation ++ shapeTransformation

      //TODO, this should not be done here but in a listener of transformations, that would also solve the problem that shapeTransformation is not updated if the transformations contain a shape related transformation
      val totalTransformation =
        (transformations foldLeft propertyDerivedTransformation)(_ ++ _)

      totalTransformation
    }
  }

  writableShapeTransformation <==
    rotation | rotationAxis |
    scaleX | scaleY | scaleZ |
    width | height map {
      case (
        rotation, rotationAxis,
        scaleX, scaleY, scaleZ,
        width, height) => {

        val pivot = Point(
          width / 2d,
          height / 2d)

        Rotate(rotation, rotationAxis, pivot) ++
          Scale(scaleX, scaleY, scaleZ, pivot)
      }
    }

  writablePositionTransformation <== x | translateX | y | translateY | translateZ map {
    case (x, translateX, y, translateY, translateZ) =>
      Translate(x + translateX, y + translateY, translateZ)
  }
  
  writableUntransformedBounds <== width | height map {
    case (width, height) => Bounds(0, 0, width, height)
  }

  writableBounds <== totalTransformation | untransformedBounds map {
    case (totalTransformation, untransformedBounds) =>
      untransformedBounds transform totalTransformation
  }
  
  writableShapeBounds <== shapeTransformation | untransformedBounds map {
    case (shapeTransformation, untransformedBounds) =>
      untransformedBounds transform shapeTransformation
  }
}