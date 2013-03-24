package ee.ui.members

import ee.ui.system.AccessRestriction

trait ReadOnlyProperty[T] {
  val defaultValue:T
  def value:T
  protected def value_=(value:T):Unit
}

object ReadOnlyProperty {
  def apply[T](defaultValue:T):ReadOnlyProperty[T] = new Property(defaultValue)
  
  def setValue[T](readOnlyProperty:ReadOnlyProperty[T], value:T)(implicit ev:AccessRestriction) = 
    readOnlyProperty.value = value
  
}