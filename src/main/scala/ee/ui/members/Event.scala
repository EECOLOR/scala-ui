package ee.ui.members

import ee.ui.members.detail.CombinedEventBase
import ee.ui.members.detail.Observers

trait Event[A] extends ReadOnlyEvent[A] with Observers[A => Unit] { self =>

  def fire(information: A) =
    notify(_(information))

  def |[B <: C, A1 >: A <: C, C](that: Event[B]):Event[C] =
    new CombinedEventBase[A1, B, C](self.asInstanceOf[Event[A1]], that) with Event[C] {
      override def fire(information: C) = super[Event].fire(information)
      override def observe(observer:C => Unit) = super[CombinedEventBase].observe(observer)
    }
}

object Event {
  def apply[T]() = new Event[T] {}
}