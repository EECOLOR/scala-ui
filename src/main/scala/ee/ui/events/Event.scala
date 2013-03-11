package ee.ui.events

import ee.ui.system.RestrictedAccess
import ee.ui.observables.CanMapObservable
import ee.ui.observables.Observable
import ee.ui.observables.Observer
import ee.ui.observables.Subscription
import scala.language.higherKinds
import ee.ui.observables.CanCombineObservable

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