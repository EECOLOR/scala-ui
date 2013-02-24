package ee.ui.bindings

import ee.ui.observables.ObservableValue
import ee.ui.properties.Variable

trait Binding {
  def unbind(): Unit
}

trait BasicBinding[S, T] extends Binding {

  def default:T
  def source:ObservableValue[S]
  def target:Variable[T]
  def map: S => T

  target.value = default
  
  val subscription = source.change { value =>
    target.value = map(value)
  }

  def unbind() = subscription.unsubscribe()
}

case class SimpleBinding[S, T](
    source: ObservableValue[S], 
    target: Variable[T], 
    default: T)(implicit ev: S <:< T) extends BasicBinding[S, T] {
  
  val map = { value:S => 
      value:T
    }
}

//TODO is this used?
case class MappedBinding[S, T](
    source: ObservableValue[S], 
    target: Variable[T], 
    default: T,
    map: S => T) extends BasicBinding[S, T] {
}