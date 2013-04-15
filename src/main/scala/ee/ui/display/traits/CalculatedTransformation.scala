package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.primitives.transformation.Identity
import ee.ui.primitives.Transformation
import ee.ui.members.ReadOnlyProperty
import ee.ui.primitives.Point
import ee.ui.primitives.transformation.Rotate
import ee.ui.primitives.transformation.Scale
import ee.ui.primitives.transformation.Translate

trait CalculatedTransformation extends ReadOnlyPosition with ReadOnlySize with ReadOnlyTranslation with ReadOnlyScaling with ReadOnlyRotation with ReadOnlyTransformations {

  protected val positionTransformation = Property[Transformation](Identity)

  positionTransformation <== x | translateX | y | translateY | translateZ map {
    case (x, translateX, y, translateY, translateZ) =>
      Translate(x + translateX, y + translateY, translateZ)
  }

  protected val shapeTransformation = Property[Transformation](Identity)

  shapeTransformation <==
    /*rotation | rotationAxis |*/
    scaleX | scaleY | scaleZ |
    width | height map {
      case (
        /*rotation, rotationAxis,*/
        scaleX, scaleY, scaleZ,
        width, height) => {

        val pivot = Point(width / 2d, height / 2d)

        Scale(scaleX, scaleY, scaleZ, pivot)/* ++ Rotate(rotation, rotationAxis, pivot)*/
      }
    }

  protected val _totalTransformation = Property[Transformation](Identity)
  val totalTransformation: ReadOnlyProperty[Transformation] = _totalTransformation

  _totalTransformation <== shapeTransformation | positionTransformation map {
    case (shapeTransformation, positionTransformation) => {
      val propertyDerivedTransformation: Transformation =
        shapeTransformation ++ positionTransformation

      //TODO, this should not be done here but in a listener of transformations, that would also solve the problem that shapeTransformation is not updated if the transformations contain a shape related transformation
        /*
      val totalTransformation =
        (transformations foldLeft propertyDerivedTransformation)(_ ++ _)
      totalTransformation
*/
        propertyDerivedTransformation
    }
  }
}
