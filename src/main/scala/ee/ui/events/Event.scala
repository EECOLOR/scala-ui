package ee.ui.events

import ee.ui.observable.Observable
import ee.ui.observable.ObservableValue
import ee.ui.observable.Subscription
import ee.ui.system.RestrictedAccess
import ee.ui.observable.ObservableValue
import ee.ui.observable.WrappedSubscription
import scala.language.implicitConversions

class ReadOnlyEvent[T] extends Observable[T] {
  def apply(listener: T => Unit): Subscription = this observe listener
  def apply(listener: => Unit): Subscription = this observe { value => listener }
  def in[R](listener: PartialFunction[T, R]): Observable[R] with Subscription = this collect listener

  private[events] def fire(information: T): Unit = notify(information)
}

class Event[T] extends ReadOnlyEvent[T] {
  override def fire(information: T): Unit = super.fire(information)
}

class EventProxy[T](target: ReadOnlyEvent[T]) extends Event[T] {

  override def fire(information: T): Unit = target.fire(information)
}