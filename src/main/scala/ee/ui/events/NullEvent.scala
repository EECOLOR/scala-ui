package ee.ui.events

import ee.ui.system.RuntimeError
import ee.ui.members.Event
import ee.ui.members.details.Subscription

class NullEvent extends Event[Null] {
  private def error(methodType:String) = 
    throw new UnsupportedOperationException(s"This event has no information, it's not possible to register a $methodType that uses information")
  
  def apply(observer: Null => Unit)(implicit ev:RuntimeError): Subscription = throw error("listener")

  def fire:Unit = fire(null)
}

