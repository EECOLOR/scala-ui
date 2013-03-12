package ee.ui.members

import ee.ui.system.RestrictedAccess
import ee.ui.members.details.CanMapObservable
import ee.ui.members.details.Observable
import ee.ui.members.details.CanCombineObservable
import ee.ui.events.Observer
import ee.ui.members.details.Subscription

import scala.language.higherKinds

trait Event[T] extends ReadOnlyEvent[T] {
  def fire(information: T): Unit = ReadOnlyEvent.notify(this, information)(RestrictedAccess)
}

object Event {
  def apply[T] = new Event[T] {}

  implicit def eventObservableMapping[O[X] <: Observable[X]] =
    new CanMapObservable[O, Event] {
      def map[T, R](observable: O[T])(f: Observer[R] => Observer[T]): Event[R] =
        new Observable.Wrapped(observable, f) with Event[R] {
          override def observe(observer: Observer[R]): Subscription = super[Wrapped].observe(observer)
        }
    }

  implicit def eventObservableCombination[O1[X] <: Observable[X], O2[X] <: Observable[X]] =
    new CanCombineObservable[O1, O2, Event] {
      def combine[R, T1 <: R, T2 <: R](observable1: O1[T1], observable2: O2[T2]): Event[R] =
        new Observable.Default[R] with Event[R] {
          observable1 observe notify
          observable2 observe notify
        }
    }
}