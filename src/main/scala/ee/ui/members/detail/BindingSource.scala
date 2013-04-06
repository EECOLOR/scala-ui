package ee.ui.members.detail

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import scala.language.implicitConversions
import ee.ui.members.ReadOnlyEvent

abstract class BindingSource[T](source:ReadOnlyProperty[T]) {
  
  def bindTo(property: Property[T]): Unit
  
  def filter(f:T => Boolean):BindingSource[T] =
    new BindingSource[T](source) {
      def bindTo(property: Property[T]) = {
        if (f(source.value)) property.value = source.value

        val filteredChange = source.change filter f
        filteredChange { property.value = _ }
      }
    } 
}
