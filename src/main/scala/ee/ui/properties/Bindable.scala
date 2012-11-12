package ee.ui.properties

import language.implicitConversions
import ee.ui.ObservableValue

//TODO bindings were created in a hurry, check if they need to be remodeled
trait Bindable[T] extends WritableProperty[T] {

  def bindTo(binding: Binding[T]): Unit = binding bind { value = _ }

  def ==>(property: Bindable[T]): Unit = property bindTo this
  def <== = bindTo _

}

trait Binding[T] {
  def bind(applyValue: T => Unit):Unit
}

class SimpleBinding[T](observableValue: ObservableValue[T]) extends Binding[T] {
  def bind(applyValue: T => Unit) =
    observableValue onValueChange applyValue

  def when(condition: T => Boolean) = 
    new ConditionalBinding(observableValue, condition)

  def map[A](mapping: T => A) = 
    new MappingBinding(observableValue, mapping)
}

object Binding {
  implicit def observableValueToSimpleBinding[T](observableValue: ObservableValue[T]): SimpleBinding[T] = new SimpleBinding(observableValue)
}

class ConditionalBinding[T](
  observableValue: ObservableValue[T], condition: T => Boolean) extends Binding[T] {

  def bind(applyValue: T => Unit) =
    observableValue onValueChangeIn {
      case n if (condition(n)) => applyValue(n)
    }
}

class MappingBinding[T, A](
		observableValue: ObservableValue[T], mapping: T => A) extends Binding[A] {
	
	def bind(applyValue: A => Unit) =
		observableValue onValueChange (mapping andThen applyValue)
}
