package ee.ui.properties

import ee.ui.bindings.BindableVariable
import ee.ui.observables.ObservableVariable

class Property[T](val default:T) extends ReadOnlyProperty[T] with ObservableVariable[T] with BindableVariable[T]

object Property {
  def apply[T](defaultValue:T) = new Property[T](defaultValue)
}