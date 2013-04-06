package ee.ui.members

import scala.collection.mutable.ListBuffer
import ee.ui.system.AccessRestriction
import ee.ui.members.detail.Subscription
import ee.ui.members.detail.Subscription
import scala.language.implicitConversions
import ee.ui.members.detail.BindingSource

trait ReadOnlyEvent[T] { self =>

  def observe(observer: T => Unit): Subscription

  def apply(observer: T => Unit) = observe(observer)

  protected def fire(information: T): Unit

  protected def mappedReadOnlyEvent[R](wrapped: (R => Unit) => (T => Unit)): ReadOnlyEvent[R] =
    new ReadOnlyEvent[R] {
      def observe(observer: R => Unit): Subscription = {
        self observe wrapped(observer)
      }

      protected def fire(information: R): Unit =
        throw new UnsupportedOperationException("The fire method is not supported on a mapped instance")
    }

  def collect[R](f: PartialFunction[T, R]) =
    mappedReadOnlyEvent[R] { observer =>
      information => if (f isDefinedAt information) observer(f(information))
    }

  def map[R](f: T => R): ReadOnlyEvent[R] =
    mappedReadOnlyEvent[R] { observer =>
      information => observer(f(information))
    }

  def filter(f: T => Boolean): ReadOnlyEvent[T] =
    mappedReadOnlyEvent[T] { observer =>
      information => if (f(information)) observer(information)
    }

}

object ReadOnlyEvent {
  def apply[T](): ReadOnlyEvent[T] = Event[T]()
  def fire[T](r: ReadOnlyEvent[T], information: T)(implicit ev: AccessRestriction) =
    r.fire(information)

  implicit def toBindingSource[T](source: ReadOnlyEvent[T]): BindingSource[Option[T]] =
    ReadOnlyProperty.toBindingSource(source)

  implicit def toSignal(event: ReadOnlyEvent[_]): ReadOnlySignal =
    new ReadOnlySignal {
      def observe(observer: => Unit): Subscription =
        event.observe(ignored => observer)

      protected def fire() =
        throw new UnsupportedOperationException("The fire method is not supported on a event wrapping instance")
    }
}