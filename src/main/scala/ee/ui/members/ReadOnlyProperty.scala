package ee.ui.members

import ee.ui.system.AccessRestriction
import scala.language.implicitConversions

trait ReadOnlyProperty[T] {
  val defaultValue: T
  def value: T
  protected def value_=(value: T): Unit
  val change = ReadOnlyEvent[T]()
}

object ReadOnlyProperty {
  def apply[T](defaultValue: T): ReadOnlyProperty[T] = new Property(defaultValue)

  def setValue[T](readOnlyProperty: ReadOnlyProperty[T], value: T)(implicit ev: AccessRestriction) =
    readOnlyProperty.value = value

  implicit def propertyToValue[T](p: ReadOnlyProperty[T]): T = p.value
  
  def unapply[T](r:ReadOnlyProperty[T]):Option[T] = 
    Option(r) map (_.value)
}