package ee.ui.events

import ee.ui.traits.RuntimeError

class NullEvent extends Event[Null] {
  private def error(methodType:String) = 
    throw new UnsupportedOperationException(s"This event has no information, it's not possible to register a $methodType that uses information")
  
  def apply(listener: Null => Unit)(implicit ev:RuntimeError): Unit = throw error("listener") 
  def in(listener: PartialFunction[Null, Unit])(implicit ev:RuntimeError): Unit = throw error("partial listener")

  def handle(handler: Handler)(implicit ev:RuntimeError): Unit = throw error("handler")
  def handle(handler: => Boolean)(implicit ev:RuntimeError): Unit = super.handle(handler)
  def handleIn(handler: PartialFunction[Null, Boolean])(implicit ev:RuntimeError):Unit = throw error("partial handler")
  
  def fire:Unit = fire(null)
}