package ee.ui.members

import ee.ui.system.RestrictedAccess
import ee.ui.members.details.Observable
import ee.ui.events.Observer
import ee.ui.members.details.Subscription
import scala.language.higherKinds
import ee.ui.members.details.CanTypeObservable
import ee.ui.members.details.Notifyable

trait Event[T] extends ReadOnlyEvent[T] with Notifyable[T] {
  def fire(information: T): Unit = notify(information)
}

object Event {
  def apply[T]: Event[T] =
    new Event[T] with Observable.Default[T]

}