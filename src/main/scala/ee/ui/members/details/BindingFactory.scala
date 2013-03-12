package ee.ui.members.details

import scala.annotation.implicitNotFound
import scala.language.higherKinds

@implicitNotFound(msg = "Can not find a suitable BindingFactory to create a binding from ${S} to ${T}")
trait BindingFactory[S, T] {
  def create(source: S, target: T): Binding
}

trait BindingFactoryLowerPriority {

  implicit def optionalBinding[T, O <: Observable[T]] =
    new BindingFactory[O, Variable[Option[T]]] {
      def create(source: O, target: Variable[Option[T]]) =
        SimpleBinding(source, target, None)
    }

  implicit def exactBinding[T, O <: ObservableValue[T]] =
    new BindingFactory[O, Variable[T]] {
      def create(source: O, target: Variable[T]) =
        SimpleBinding[T, T](source, target, source.value)
    }

}

object BindingFactory extends BindingFactoryLowerPriority {
  implicit def inherritedBinding[S <: T, T, O[X <: S] <: ObservableValue[X]]: BindingFactory[O[S], Variable[T]] =
    new BindingFactory[O[S], Variable[T]] {
      def create(source: O[S], target: Variable[T]) =
        SimpleBinding[S, T](source, target, source.value)
    }

}