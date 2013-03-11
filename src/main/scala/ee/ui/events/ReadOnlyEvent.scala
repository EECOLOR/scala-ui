package ee.ui.events
import ee.ui.observables.Observer
import ee.ui.observables.CanMapObservable
import ee.ui.observables.Subscription
import ee.ui.observables.Observable
import ee.ui.system.AccessRestriction
import scala.language.higherKinds
import ee.ui.system.RestrictedAccess
import ee.ui.observables.CanCombineObservable

trait ReadOnlyEvent[T] extends Observable.Default[T] {
  def apply(observer: T => Unit): Subscription = observe(observer)
  def apply(observer: => Unit): Subscription = observe(_ => observer)
}

object ReadOnlyEvent {
  def apply[T] = new ReadOnlyEvent[T] {}

  def notify[T](event: ReadOnlyEvent[T], information: T)(implicit ev: AccessRestriction) =
    event notify information

  // TODO can't we just use the ones from Event? 
  implicit def readOnlyEventObservableMapping[O[X] <: Observable[X]]:CanMapObservable[O, ReadOnlyEvent] =
    Event.eventObservableMapping[O]

  implicit def readOnlyEventObservableCombination[O1[X] <: Observable[X], O2[X] <: Observable[X]]:CanCombineObservable[O1, O2, ReadOnlyEvent] =
    Event.eventObservableCombination[O1, O2]
}