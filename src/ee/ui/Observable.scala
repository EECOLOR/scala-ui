package ee.ui

trait Observable[T] {
  type Handler = T => Boolean
  type Listener = T => Unit

  private var handlers: Seq[Handler] = Seq.empty
  private var listeners: Seq[Listener] = Seq.empty

  protected def notifyHandlers(information: T) =
    handlers forall { _(information) }

  private def notifyListeners(information: T) =
    listeners foreach { _(information) }

  protected def handle(handler: Handler): Unit = handler +: handlers
  protected def handle(handler: => Boolean): Unit = handle(information => handler)
  protected def handleIn(handler: PartialFunction[T, Boolean]): Unit =
    handle { i =>
      if (handler isDefinedAt i) handler(i)
      else true
    }

  protected def listen(listener: Listener): Unit = listener +: listeners
  protected def listen(listener: => Unit): Unit = listen(information => listener)

  protected def listenIn(listener: PartialFunction[T, Unit]): Unit =
    listen { i =>
      if (listener isDefinedAt i) listener(i)
    }

  protected def notify(information: T, apply: => Unit): Unit = {
    if (notifyHandlers(information)) {
      apply
      notifyListeners(information)
    }
  }

  protected def notify(information: T): Unit = notify(information, {})

}