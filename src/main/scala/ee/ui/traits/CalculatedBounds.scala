package ee.ui.traits

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.primitives.Transformation
import ee.ui.primitives.Translate
import ee.ui.primitives.Bounds
import ee.ui.primitives.Identity
import ee.ui.properties.PropertyGroup
import ee.ui.primitives.Rotate
import ee.ui.primitives.Scale
import ee.ui.primitives.Point

trait CalculatedBounds extends LayoutPosition with LayoutSize
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

  private val totalTransformationProperties =
    PropertyGroup(shapeTransformation, positionTransformation) ~> {
      (shapeTransformation, positionTransformation) =>

        val propertyDerivedTransformation: Transformation =
          positionTransformation ++ shapeTransformation

        //TODO, this should not be done here but in a listener of transformations, that would also solve the problem that shapeTransformation is not updated if the transformations contain a shape related transformation
        val totalTransformation =
          (transformations foldLeft propertyDerivedTransformation)(_ ++ _)

        writableTotalTransformation.value = totalTransformation
    }

  private val shapeTransformationProperties = PropertyGroup(
    rotation, rotationAxis,
    scaleX, scaleY, scaleZ,
    width, height) ~> {
      (rotation, rotationAxis,
      scaleX, scaleY, scaleZ,
      width, height) =>

        val pivot = Point(
          width / 2d,
          height / 2d)

        writableShapeTransformation.value =
          Rotate(rotation, rotationAxis, pivot) ++
            Scale(scaleX, scaleY, scaleZ, pivot)

    }

  private val positionTransformationProperties =
    PropertyGroup(x, translateX, y, translateY, translateZ) ~> {
      (x, translateX, y, translateY, translateZ) =>

        writablePositionTransformation.value =
          Translate(x + translateX, y + translateY, translateZ)
    }

  private val untransformedBoundsProperties =
    PropertyGroup(width, height) ~> {
      (width, height) => writableUntransformedBounds.value = Bounds(0, 0, width, height)
    }

  private val boundsProperties =
    PropertyGroup(totalTransformation, untransformedBounds) ~> {
      (totalTransformation, untransformedBounds) =>

        writableBounds.value = untransformedBounds transform totalTransformation
    }

  private val shapeBoundsProperties =
    PropertyGroup(shapeTransformation, untransformedBounds) ~> {
      (shapeTransformation, untransformedBounds) =>

        writableShapeBounds.value = untransformedBounds transform shapeTransformation
    }
}