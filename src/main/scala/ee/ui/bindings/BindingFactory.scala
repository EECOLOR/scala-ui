package ee.ui.bindings

import scala.annotation.implicitNotFound
import ee.ui.observables.ObservableValue
import ee.ui.properties.Variable
import ee.ui.observables.Observable
import scala.language.higherKinds

@implicitNotFound(msg = "Can not find a suitable BindingFactory to create a binding from ${S} to ${T}")
trait BindingFactory[S, T] {
  def create(source: S, target: T): Binding
}

trait BindingFactoryLowerPriority {

  implicit def optionalBinding[T, O <: Observable[T]] = new BindingFactory[O, Variable[Option[T]]] {
    def create(source: O, target: Variable[Option[T]]) =
      SimpleBinding(source, target, None)
  }

  implicit def simpleBinding[S, T, O <: ObservableValue[S]](implicit ev: S <:< T): BindingFactory[O, Variable[T]] =
    new BindingFactory[O, Variable[T]] {
      def create(source: O, target: Variable[T]) =
        SimpleBinding[S, T](source, target, source.value)
    }

}

object BindingFactory extends BindingFactoryLowerPriority {

  implicit def simpleBinding[T, O <: ObservableValue[T]]: BindingFactory[O, Variable[T]] =
    new BindingFactory[O, Variable[T]] {
      def create(source: O, target: Variable[T]) =
        SimpleBinding[T, T](source, target, source.value)
    }
}