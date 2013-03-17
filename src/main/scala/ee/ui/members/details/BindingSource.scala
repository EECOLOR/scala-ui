package ee.ui.members.details

import scala.language.higherKinds
import scala.language.implicitConversions

trait BindingSource[OS[~], S] { self =>
  val source: OS[S]

  def create[T](target: BindableVariable[T])(implicit factory: BindingFactory[OS, Variable[T], S]) =
    factory create (source, target)

  def filter(f: S => Boolean) =
    new BindingSource[OS, S] {
      val source = self.source

      override def create[T](target: BindableVariable[T])(implicit factory: BindingFactory[OS, Variable[T], S]) =
        factory create (source, target, f)
    }
}

object BindingSource {
  def apply[OS[~], S](s: OS[S]) =
    new BindingSource[OS, S] {
      val source = s
    }

  implicit def observableToBindingSource[O[~] <: Observable[~], T](o: O[T]): BindingSource[O, T] =
    BindingSource(o)
}
