package ee.ui.events

import ee.ui.system.RestrictedAccess
import ee.ui.system.AccessRestriction
import ee.ui.members.Event
import ee.ui.members.ReadOnlyEvent
import ee.ui.members.details.Observable
import ee.ui.members.details.Notifyable

class EventProxy[T](target: ReadOnlyEvent[T] with Notifyable[T])(implicit ev:AccessRestriction) extends Event[T] {
  
  def notify(information:T) = Notifyable.notify(target, information)
  def observe(o:Observer[T]) = target.observe(o)
}