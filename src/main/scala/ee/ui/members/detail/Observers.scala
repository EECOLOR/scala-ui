package ee.ui.members.detail

import scala.collection.mutable.ListBuffer

trait Observers[T] {
  protected val observers = ListBuffer.empty[T]
  
  def observe(observer: T): Subscription = {
    observers += observer
    new Subscription {
      def unsubscribe() = observers -= observer
      def enable() = if (!(observers contains observer)) observers += observer
      def disable() = unsubscribe()
    }
  }

  protected def notify(invoke:T => Unit) = 
    observers foreach invoke
}