package ee.ui.events

import ee.ui.Observable

class Event[T] extends Observable[T] {
  def apply(listener: T => Unit): Unit = super.listen(listener)
  def apply(listener: => Unit): Unit = super.listen(listener)
  def in(listener: PartialFunction[T, Unit]): Unit = super.listenIn _

  override def handle(handler: Handler): Unit = super.handle(handler)
  override def handle(handler: => Boolean): Unit = super.handle(handler)
  def handleIn = super.handleIn _
  
  def fire(information: T):Unit = notify(information)
}