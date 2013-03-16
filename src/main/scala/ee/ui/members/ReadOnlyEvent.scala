package ee.ui.members

import ee.ui.members.details.Observable
import ee.ui.system.AccessRestriction
import ee.ui.members.details.Subscription
import scala.language.higherKinds
import ee.ui.members.details.CanTypeObservable
import ee.ui.members.details.Notifyable

trait ReadOnlyEvent[T] extends Observable[T] {
  def apply(observer: T => Unit): Subscription = observe(observer)
  def apply(observer: => Unit): Subscription = observe(_ => observer)
}

object ReadOnlyEvent {

  def apply[T]: ReadOnlyEvent[T] with Notifyable[T] =
    new ReadOnlyEvent[T] with Observable.Default[T]

  implicit def readOnlyEventObservableTyper[O[X] <: Observable[X]] =
    new CanTypeObservable[O, ReadOnlyEvent] {
      def typed[T](source: Observable[T]):ReadOnlyEvent[T] = 
        new Observable.Proxy(source) with ReadOnlyEvent[T]
    }

}