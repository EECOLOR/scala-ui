package ee.ui.members.details

import ee.ui.system.AccessRestriction

trait Notifyable[T] {
  protected def notify(information: T): Unit
}

object Notifyable {
  def notify[T](observable: Notifyable[T], information: T)(implicit ev: AccessRestriction) =
    observable notify information
}