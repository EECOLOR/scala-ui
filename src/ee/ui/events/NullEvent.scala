package ee.ui.events

class NullEvent extends Event[Null] {
  private def error(methodType:String) = 
    throw new UnsupportedOperationException(s"This event has no information, it's not possible to register a $methodType that uses information")
  
  override def apply(listener: Null => Unit): Unit = throw error("listener") 
  override def apply(listener: => Unit): Unit = super.listen(listener)
  override def in(listener: PartialFunction[Null, Unit]): Unit = throw error("partial listener")

  override def handle(handler: Handler): Unit = throw error("handler")
  override def handle(handler: => Boolean): Unit = super.handle(handler)
  override def handleIn = throw error("partial handler")
  
  def fire:Unit = fire(null)
}