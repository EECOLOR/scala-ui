package ee.ui.properties

trait Observable[T] {
    type Handler = (T, T, this.type) => Boolean
    type Listener = (T, T, this.type) => Unit

    private var handlers: Seq[Handler] = Seq.empty

    protected def notify(oldValue: T, newValue: T) = 
        handlers.forall { handler =>
               handler(oldValue, newValue, this)
        }

    def handleChange(handler: Handler): Unit = handler +: handlers
    def handleChangeMatch(handler: PartialFunction[(T, T, Observable[T]), Boolean]): Unit =
        handleChange { (o, n, p) =>
            val arguments = (o, n, p)
            if (handler isDefinedAt arguments) handler(arguments)
            else true
        }

    def onChange(handler: Listener): Unit =
        handleChange { (o, n, p) =>
            handler(o, n, p)
            true
        }
    def onChangeMatch(handler: PartialFunction[(T, T, Observable[T]), Unit]): Unit =
    	onChange { (o, n, p) =>
    	    val arguments = (o, n, p)
    		if (handler isDefinedAt arguments) handler(arguments)
    	}
    
    def forNewValue(handler: T => Unit):Unit = 
        onChange {(o, n, p) => handler(n) }
    
}