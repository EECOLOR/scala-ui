package ee.ui

import ee.ui.properties.ReadOnlyProperty
import ee.ui.properties.Property
import ee.ui.traits.LayoutSize
import ee.ui.traits.LayoutPosition
import ee.ui.layout.LayoutClient
import language.implicitConversions
import ee.ui.traits.Translation
import ee.ui.traits.Scaling
import ee.ui.traits.Rotation
import ee.ui.traits.Transformations
import ee.ui.primitives.Transformation
import ee.ui.primitives.Affine
import ee.ui.primitives.Translate
import ee.ui.primitives.Rotate
import ee.ui.primitives.Scale
import ee.ui.primitives.Translate
import ee.ui.primitives.Bounds
import ee.ui.properties.PropertyChangeCollector
import ee.ui.properties.PropertyChangeCollector._
import ee.ui.traits.Pulse

abstract class Node extends LayoutClient with LayoutPosition with LayoutSize 
	with Translation with Scaling with Rotation with Transformations with Pulse {

  private val writableParent = new Property[Option[Group]](None) with ParentProperty
  val parent: ParentProperty = writableParent

  private val writableTotalTransformation = new Property[Transformation](Translate(x, y, 0))
  val totalTransformation: ReadOnlyProperty[Transformation] = writableTotalTransformation

  private val writableBounds = new Property[Bounds](Bounds(x, y, width, height))
  val bounds: ReadOnlyProperty[Bounds] = writableBounds
  
  onPulse {
    transformationPropertiesChangeCollector.applyChanges
    boundsPropertiesChangeCollector.applyChanges
  }
  
  def scaleOrRotationChanged =
    scaleX.isChanged || scaleY.isChanged || scaleZ.isChanged ||
      rotation.isChanged || rotationAxis.isChanged

  private val transformationPropertiesChangeCollector = new PropertyChangeCollector(
    (x, y, translateX, translateY, translateZ,
      rotation, rotationAxis,
      scaleX, scaleY, scaleZ) ~> {
        (x, y, translateX, translateY, translateZ,
        rotation, rotationAxis,
        scaleX, scaleY, scaleZ) =>

          val newX = translateX + x
          val newY = translateY + y

          val nodeTransformations =
            if (scaleOrRotationChanged) {
              val pivotX = width / 2d + newX
              val pivotY = height / 2d + newY

              Translate(newX, newY, translateZ) ++
                Rotate(rotation, rotationAxis, pivotX, pivotY) ++
                Scale(scaleX, scaleY, scaleZ)

            } else
              Translate(newX, newY, translateZ)

          val totalTransformation =
            (transformations foldLeft nodeTransformations)(_ ++ _)

          writableTotalTransformation.value = totalTransformation
      })

  private val boundsPropertiesChangeCollector = new PropertyChangeCollector(
    (width, height, totalTransformation) ~> {
      (width, height, totalTransformation) =>

        val bounds = Bounds(0, 0, width, height) transform totalTransformation
        
        writableBounds.value = bounds
    })

}

object Node {
  def setParent(node: Node, parent: Option[Group]) =
    node.writableParent.value = parent
}

trait ParentProperty extends ReadOnlyProperty[Option[Group]]

object ParentProperty {
  implicit def optionalPropertyToValue(property: ParentProperty): Group =
    property.value.getOrElse(
      throw new NoSuchElementException(
        "The node has no parent. This might be caused by the fact that it's the " +
          "root property or that it has not been added to the children of a Group. " +
          "The parent property is defined as an Option[Group] so you could use the " +
          "Option methods to handle this if needed. This implicit conversion is " +
          "added as a convenience for parent specifically."))
}