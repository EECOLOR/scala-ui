package ee.ui

import scala.collection.mutable.ListBuffer

trait Observable[T] {
  type Handler = T => Boolean
  type Listener = T => Unit

  private val handlers = ListBuffer[Handler]()
  private val listeners = ListBuffer[Listener]()

  protected def notifyHandlers(information: T) =
    handlers forall { _(information) }

  private def notifyListeners(information: T) =
    listeners foreach {
	  _(information) 
    }

  protected def handle(handler: Handler): Unit = handlers += handler
  protected def handle(handler: => Boolean): Unit = handle(information => handler)
  protected def handleIn(handler: PartialFunction[T, Boolean]): Unit =
    handle { i =>
      if (handler isDefinedAt i) handler(i)
      else true
    }

  protected def listen(listener: Listener): Unit = listeners += listener
  protected def listen(listener: => Unit): Unit = listen(information => listener)

  protected def listenIn(listener: PartialFunction[T, Unit]): Unit =
    listen { i =>
      if (listener isDefinedAt i) listener(i)
    }

  protected def notify(information: T, apply: => Unit): Unit = {
    //println("Observable notify " + information + " -- " + this)
    if (notifyHandlers(information)) {
      apply
      notifyListeners(information)
    }
  }

  protected def notify(information: T): Unit = notify(information, {})

}