package ee.ui.members

import scala.language.implicitConversions
import ee.ui.system.RestrictedAccess

case class Property[T](defaultValue: T) extends ReadOnlyProperty[T] {
  private var _value: T = defaultValue
  def value = _value
  def value_=(value: T): Unit =
    if (value != _value) {
      _value = value
      ReadOnlyEvent.fire(change, value)(RestrictedAccess)
    }

  val change = ReadOnlyEvent[T]()
}

object Property {
  implicit def propertyToValue[T](p: Property[T]): T = p.value
}