package ee.ui.properties

trait Observable[T] {
  type Handler = (T, T, this.type) => Boolean
  type Listener = (T, T, this.type) => Unit

  private var handlers: Seq[Handler] = Seq.empty
  private var listeners: Seq[Listener] = Seq.empty

  private def checkHandlers(oldValue: T, newValue: T) =
    handlers.forall { handler =>
      handler(oldValue, newValue, this)
    }

  private def notify(oldValue: T, newValue: T) =
    listeners.foreach { listener =>
      listener(oldValue, newValue, this)
    }

  def onChange(handler: Handler): Unit = handler +: handlers
  def onChangeMatch(handler: PartialFunction[(T, T, Observable[T]), Boolean]): Unit =
    onChange { (o, n, p) =>
      val arguments = (o, n, p)
      if (handler isDefinedAt arguments) handler(arguments)
      else true
    }

  def onChanged(listener: Listener): Unit = listener +: listeners
  def onChangedMatch(listener: PartialFunction[(T, T, Observable[T]), Unit]): Unit =
    onChanged { (o, n, p) =>
      val arguments = (o, n, p)
      if (listener isDefinedAt arguments) listener(arguments)
    }

  def forNewValue(listener: T => Unit): Unit = onChanged { (o, n, p) => listener(n) }
  def forNewValueMatch(listener: PartialFunction[T, Unit]): Unit =
    forNewValue { n =>
      if (listener isDefinedAt n) listener(n)
    }
  
   def valueChange(oldValue:T, newValue: T)(applyValue: => Unit) = {
		  if (oldValue != newValue && checkHandlers(oldValue, newValue)) {
			  applyValue
			  notify(oldValue, newValue)
		  }
  }


}