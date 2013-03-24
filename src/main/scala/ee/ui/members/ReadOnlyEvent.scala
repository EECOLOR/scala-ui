package ee.ui.members

import scala.collection.mutable.ListBuffer
import ee.ui.system.AccessRestriction
import ee.ui.members.detail.Subscription
import ee.ui.members.detail.Subscription

trait ReadOnlyEvent[T] {
  val observers = ListBuffer[T => Unit]()
  
  def observe(observer:T => Unit):Subscription = {
      observers += observer
      new Subscription {
        def unsubscribe() = observers -= observer
      }
  } 
    
  def apply(observer:T => Unit) = observe(observer)
  
  protected def fire(information:T):Unit
}

object ReadOnlyEvent {
  def apply[T]():ReadOnlyEvent[T] = Event[T]()
  def fire[T](r:ReadOnlyEvent[T], information:T)(implicit ev:AccessRestriction) = 
    r.fire(information)
}