package ee.ui

import ee.ui.properties.ReadOnlyProperty
import ee.ui.properties.Property
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
import ee.ui.properties.PropertyChangeCollector
import ee.ui.properties.PropertyChangeCollector._
import ee.ui.properties.PropertyGroup
import ee.ui.primitives.Identity
import ee.ui.traits.CalculatedBounds
import ee.ui.traits.MouseTraits
import ee.ui.traits.Focus
import ee.ui.traits.Position
import ee.ui.traits.Size

//TODO should we mix all of these in or let the user (or component creator) 
//mix them in (probably the last)
abstract class Node extends LayoutClient with Position with Size
  with Translation with Scaling with Rotation with Transformations 
  with CalculatedBounds with MouseTraits with Focus {

  private val writableParent = new Property[Option[Group]](None) with ParentProperty
  val parent: ParentProperty = writableParent

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