package ee.ui.members

import ee.ui.members.detail.Subscription
import scala.collection.mutable.ListBuffer
import ee.ui.members.detail.Observers

case class Signal() extends ReadOnlySignal with Observers[() => Unit] {
  def observe(observer: => Unit): Subscription = 
    super.observe(() => observer)
  
  def fire() = 
    notify(_())
}

