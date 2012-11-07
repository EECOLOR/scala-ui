package ee.ui.properties

import ee.ui.Observable

trait ObservableProperty[T] extends Observable[(T, T)] {

  def onChange(handler: (T, T) => Boolean): Unit = handle(handler.tupled)
  def onChangeIn = handleIn _

  def onChanged(listener: => Unit):Unit = listen(listener)
  def onChanged(listener: (T, T) => Unit): Unit = listen(listener.tupled)
  def onChangedIn = listenIn _
  
  def ~> = forNewValue _
  def forNewValue(listener: T => Unit): Unit = listenIn { case (o, n) => listener(n) }
  def forNewValueIn(listener: PartialFunction[T, Unit]): Unit =
    listenIn { case (o, n) =>
        if (listener isDefinedAt n) listener(n)
    }

  def valueChange(oldValue: T, newValue: T)(applyValue: => Unit) = {
    notify(oldValue -> newValue, applyValue)
  }
}