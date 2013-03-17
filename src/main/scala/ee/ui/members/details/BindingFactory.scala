package ee.ui.members.details

import scala.annotation.implicitNotFound
import scala.language.higherKinds

@implicitNotFound(msg = "Can not find a suitable BindingFactory to create a binding from ${OS}[${S}] to ${OT}")
trait BindingFactory[OS[~], OT, S] {
  def create(source: OS[S], target: OT): Binding
  def create(source: OS[S], target: OT, condition: S => Boolean): Binding
}

object BindingFactory {
  implicit def optionalBinding[S <: T, T, O[~] <: Observable[~]] =
    new BindingFactory[O, Variable[Option[T]], S] {

      def create(source: O[S], target: Variable[Option[T]]) =
        SimpleBinding(source, target, None)

      def create(source: O[S], target: Variable[Option[T]], condition: S => Boolean) =
        ConditionalBinding(source, target, None, condition = { value: Option[S] =>
          value map condition getOrElse false
        })
    }

  implicit def defaultBinding[S <: T, T, O[~] <: ObservableValue[~]] =
    new BindingFactory[O, Variable[T], S] {
      def create(source: O[S], target: Variable[T]) =
        SimpleBinding(source, target, source.value)

      def create(source: O[S], target: Variable[T], condition: S => Boolean) =
        ConditionalBinding(source, target, source.value, condition)
    }
}

