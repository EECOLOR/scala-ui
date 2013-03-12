package ee.ui.members

import ee.ui.members.details.BindableVariable
import ee.ui.members.details.ObservableVariable

class Property[T](val default:T) extends ReadOnlyProperty[T] with ObservableVariable[T] with BindableVariable[T]

object Property {
  def apply[T](defaultValue:T) = new Property[T](defaultValue)
}