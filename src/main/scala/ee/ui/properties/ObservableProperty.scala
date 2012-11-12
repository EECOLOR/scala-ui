package ee.ui.properties

import ee.ui.Observable
import ee.ui.ObservableValue

trait ObservableProperty[T] extends Observable[(T, T)] with ObservableValue[T] {

  def onChange(handler: (T, T) => Boolean): Unit = handle(handler.tupled)
  def onChangeIn(handler: PartialFunction[(T, T), Boolean]): Unit = handleIn(handler)

  def onChanged(listener: => Unit):Unit = listen(listener)
  def onChanged(listener: (T, T) => Unit): Unit = listen(listener.tupled)
  def onChangedIn(listener: PartialFunction[(T, T), Unit]): Unit = listenIn(listener)
  
  def forNewValue(listener: T => Unit): Unit = listenIn { case (o, n) => listener(n) }
  def forNewValueIn(listener: PartialFunction[T, Unit]): Unit =
    listenIn { case (o, n) =>
        if (listener isDefinedAt n) listener(n)
    }

  def valueChange(oldValue: T, newValue: T)(applyValue: => Unit) = {
    notify(oldValue -> newValue, applyValue)
  }
  
  def onValueChange(listener: T => Unit):Unit = forNewValue(listener)
  def onValueChangeIn(listener: PartialFunction[T, Unit]):Unit = forNewValueIn(listener)
}