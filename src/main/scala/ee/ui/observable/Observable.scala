package ee.ui.observable

import scala.collection.mutable.ListBuffer

trait Observable[T] { self =>

  type Observer = T => Unit

  private val observers = ListBuffer[Observer]()

  def foreach(observer: Observer): Subscription = {
    val subscription = new Subscription {
      def unsubscribe(): Unit = observers -= observer
    }
    observers += observer
    subscription
  }
  def foreach(observer: => Unit): Subscription = foreach(information => observer)

  protected def notify(information: T): Unit =
    observers foreach { _(information) }

}

object Observable {
  implicit class ObservableExtension[T](o: Observable[T]) {
    def collect[R](f: PartialFunction[T, R]): Observable[R] with Subscription =
      new Observable[R] with WrappedSubscription {
        val subscription =
          o foreach { value =>
            if (f isDefinedAt value) notify(f(value))
          }
      }

    def map[R](f: T => R): Observable[R] with Subscription =
      new Observable[R] with WrappedSubscription {
        val subscription =
          o foreach { value =>
            notify(f(value))
          }
      }

    def filter(f: T => Boolean): Observable[T] with Subscription =
      new Observable[T] with WrappedSubscription {
        val subscription =
          o foreach { value =>
            if (f(value)) notify(value)
          }
      }
  }
}

trait Subscription {
  def unsubscribe(): Unit
}

trait WrappedSubscription extends Subscription {
  val subscription: Subscription

  def unsubscribe(): Unit = subscription.unsubscribe()
}

