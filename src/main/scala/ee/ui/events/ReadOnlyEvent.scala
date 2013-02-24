package ee.ui.events
import ee.ui.observables.Observer
import ee.ui.observables.CanMapObservable
import ee.ui.observables.Subscription
import ee.ui.observables.Observable
import ee.ui.system.AccessRestriction
import scala.language.higherKinds

trait ReadOnlyEvent[T] extends Observable.Default[T] {
  def apply(observer: T => Unit): Subscription = observe(observer)
  def apply(observer: => Unit): Subscription = observe(_ => observer)
  
  private[events] def notify(information: T)(implicit ev:AccessRestriction): Unit = super.notify(information)
}

object ReadOnlyEvent {
  def apply[T] = new ReadOnlyEvent[T] {}
  
  implicit def readOnlyEventObservableMapping[O[X] <: Observable[X]] =
    new CanMapObservable[O, ReadOnlyEvent] {
      def map[T, R](observable: O[T])(f: Observer[R] => Observer[T]): ReadOnlyEvent[R] =
        new Observable.Wrapped(observable, f) with ReadOnlyEvent[R] {
          override def observe(observer: Observer[R]): Subscription = super[Wrapped].observe(observer)
        }
    }

}