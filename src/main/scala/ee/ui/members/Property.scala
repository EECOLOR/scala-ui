package ee.ui.members

import scala.language.implicitConversions

case class Property[T](defaultValue:T) extends ReadOnlyProperty[T] {
  var value:T = defaultValue
  
  val change = Event[T]()
}

object Property {
  implicit def propertyToValue[T](p:Property[T]):T = p.value  
}