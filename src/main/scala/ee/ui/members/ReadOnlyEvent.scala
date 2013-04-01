package ee.ui.members

import scala.collection.mutable.ListBuffer
import ee.ui.system.AccessRestriction
import ee.ui.members.detail.Subscription
import ee.ui.members.detail.Subscription

trait ReadOnlyEvent[T] { self =>

  def observe(observer: T => Unit): Subscription

  def apply(observer: T => Unit) = observe(observer)

  protected def fire(information: T): Unit

  def collect[R](f: PartialFunction[T, R]): ReadOnlyEvent[R] =
    new ReadOnlyEvent[R] {
      def observe(observer: R => Unit): Subscription = {
        val wrapped = { information:T =>
          if (f isDefinedAt information) observer(f(information))
        }
        self observe wrapped
      } 
        
      protected def fire(information: R): Unit = 
        throw new UnsupportedOperationException("The fire method is not supported on a collecting instance")
    }
}

object ReadOnlyEvent {
  def apply[T](): ReadOnlyEvent[T] = Event[T]()
  def fire[T](r: ReadOnlyEvent[T], information: T)(implicit ev: AccessRestriction) =
    r.fire(information)
}