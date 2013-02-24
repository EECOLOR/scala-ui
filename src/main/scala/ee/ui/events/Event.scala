package ee.ui.events

import ee.ui.system.RestrictedAccess
import ee.ui.observables.CanMapObservable
import ee.ui.observables.Observable
import ee.ui.observables.Observer
import ee.ui.observables.Subscription
import scala.language.higherKinds

trait Event[T] extends ReadOnlyEvent[T] {
	def fire(information:T):Unit = notify(information)(RestrictedAccess)
}

object Event {
  def apply[T] = new Event[T] {}
  
  implicit def readOnlyEventObservableMapping[O[X] <: Observable[X]] =
    new CanMapObservable[O, Event] {
      def map[T, R](observable: O[T])(f: Observer[R] => Observer[T]): Event[R] =
        new Observable.Wrapped(observable, f) with Event[R] {
          override def observe(observer: Observer[R]): Subscription = super[Wrapped].observe(observer)
        }
    }
}