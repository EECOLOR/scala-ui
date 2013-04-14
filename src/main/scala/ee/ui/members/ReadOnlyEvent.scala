package ee.ui.members

import ee.ui.members.ReadOnlyProperty.fromReadOnlyEvent
import ee.ui.members.detail.BindingSource
import ee.ui.members.detail.CombinedEventBase
import ee.ui.members.detail.Subscription
import ee.ui.system.AccessRestriction

import scala.language.implicitConversions

trait ReadOnlyEvent[A] { self =>

  def observe(observer: A => Unit): Subscription

  def apply(observer: A => Unit) = observe(observer)

  protected def fire(information: A): Unit

  protected def mappedReadOnlyEvent[B](wrapped: (B => Unit) => (A => Unit)): ReadOnlyEvent[B] =
    new ReadOnlyEvent[B] {
      def observe(observer: B => Unit): Subscription = {
        self observe wrapped(observer)
      }

      protected def fire(information: B): Unit =
        throw new UnsupportedOperationException("The fire method is not supported on a mapped instance")
    }

  def collect[B](f: PartialFunction[A, B]) =
    mappedReadOnlyEvent[B] { observer =>
      information => if (f isDefinedAt information) observer(f(information))
    }

  def map[B](f: A => B): ReadOnlyEvent[B] =
    mappedReadOnlyEvent[B] { observer =>
      information => observer(f(information))
    }

  def filter(f: A => Boolean): ReadOnlyEvent[A] =
    mappedReadOnlyEvent[A] { observer =>
      information => if (f(information)) observer(information)
    }

  def |[B <: C, A1 >: A <: C, C](that: ReadOnlyEvent[B]):ReadOnlyEvent[C] =
    new CombinedEventBase[A1, B, C](self.asInstanceOf[ReadOnlyEvent[A1]], that) {
      protected def fire(information: C): Unit =
        throw new UnsupportedOperationException("The fire method is not supported on a combined instance")
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