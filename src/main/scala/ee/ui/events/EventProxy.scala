package ee.ui.events

import ee.ui.system.RestrictedAccess
import ee.ui.system.AccessRestriction
import ee.ui.members.Event
import ee.ui.members.ReadOnlyEvent

class EventProxy[T](target: ReadOnlyEvent[T])(implicit ev:AccessRestriction) extends Event[T] {
  override def fire(information: T): Unit = ReadOnlyEvent.notify(target, information)
}