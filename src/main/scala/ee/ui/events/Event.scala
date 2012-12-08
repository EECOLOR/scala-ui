package ee.ui.events

import ee.ui.observable.Observable
import ee.ui.observable.ObservableValue
import ee.ui.system.RestrictedAccess

class ReadOnlyEvent[T] extends Observable[T] with ObservableValue[T] {
  def apply(listener: T => Unit): Unit = super.listen(listener)
  def apply(listener: => Unit): Unit = super.listen(listener)
  def in(listener: PartialFunction[T, Unit]): Unit = super.listenIn(listener)

  override def handle(handler: Handler): Unit = super.handle(handler)
  override def handle(handler: => Boolean): Unit = super.handle(handler)
  override def handleIn(handler: PartialFunction[T, Boolean]):Unit = super.handleIn(handler)
  
  def onValueChange(listener: T => Unit): Unit = apply(listener)
  def onValueChangeIn(listener: PartialFunction[T, Unit]): Unit = in(listener)
  
  private[events] def fire(information: T):Unit = notify(information)
}

class Event[T] extends ReadOnlyEvent[T] {
  override def fire(information: T):Unit = super.fire(information)
}

class EventProxy[T](target:ReadOnlyEvent[T]) extends Event[T] {
  
  override def fire(information:T ):Unit = target.fire(information)
}