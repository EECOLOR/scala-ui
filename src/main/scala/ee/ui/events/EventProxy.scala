package ee.ui.events

import ee.ui.system.RestrictedAccess
import ee.ui.system.AccessRestriction

class EventProxy[T](target: ReadOnlyEvent[T])(implicit ev:AccessRestriction) extends Event[T] {
  override def fire(information: T): Unit = target notify information
}