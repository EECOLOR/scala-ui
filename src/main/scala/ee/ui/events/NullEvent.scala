package ee.ui.events

import ee.ui.system.RuntimeError

class NullEvent extends Event[Null] {
  private def error(methodType:String) = 
    throw new UnsupportedOperationException(s"This event has no information, it's not possible to register a $methodType that uses information")
  
  def apply(listener: Null => Unit)(implicit ev:RuntimeError): Unit = throw error("listener") 
  def in(listener: PartialFunction[Null, Unit])(implicit ev:RuntimeError): Unit = throw error("partial listener")

  def fire:Unit = fire(null)
}