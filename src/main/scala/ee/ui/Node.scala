package ee.ui

import ee.ui.properties.ReadOnlyProperty
import ee.ui.properties.Property
import ee.ui.traits.LayoutSize
import ee.ui.traits.LayoutPosition
import ee.ui.layout.LayoutClient
import language.implicitConversions

abstract class Node extends LayoutClient with LayoutPosition with LayoutSize {
  private val writeableParent = new Property[Option[Group]](None) with ParentProperty
  val parent:ParentProperty = writeableParent
}

object Node {
  def setParent(node:Node, parent:Option[Group]) = 
    node.writeableParent.value = parent 
}

trait ParentProperty extends ReadOnlyProperty[Option[Group]]

object ParentProperty {
  	implicit def optionalPropertyToValue(property:ParentProperty):Group = 
  	  property.value.getOrElse(
  	      throw new NoSuchElementException(
  	          "The node has no parent. This might be caused by the fact that it's the " +
  	          "root property or that it has not been added to the children of a Group. " +
  	          "The parent property is defined as an Option[Group] so you could use the " +
  	          "Option methods to handle this if needed. This implicit conversion is " +
  	          "added as a convenience for parent specifically."))
}