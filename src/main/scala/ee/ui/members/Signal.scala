package ee.ui.members

import ee.ui.members.detail.Observers
import ee.ui.members.detail.Subscription

class Signal extends ReadOnlySignal with Observers[() => Unit] {
  
  def observe(observer: => Unit): Subscription = 
    super.observe(() => observer)
  
  def fire() = notify(_())
}

object Signal {
  def apply() = new Signal
}