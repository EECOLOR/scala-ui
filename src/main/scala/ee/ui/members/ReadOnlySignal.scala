package ee.ui.members

import ee.ui.members.detail.Subscription
import ee.ui.system.AccessRestriction

trait ReadOnlySignal {
  def observe(observer: => Unit): Subscription
  def apply(observer: => Unit): Subscription = observe(observer)
  
  protected def fire():Unit
}

object ReadOnlySignal {
  def apply():ReadOnlySignal = Signal()
  
  def fire(signal:ReadOnlySignal)(implicit ev:AccessRestriction) = signal.fire()
}