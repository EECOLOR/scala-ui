package ee.ui.observables

import scala.collection.mutable.ListBuffer
import scala.language.higherKinds

trait Observable[T] {

  def observe(observer: Observer[T]): Subscription
}

object Observable {

  implicit def defaultObservableMapper[O[X] <: Observable[X]] =
    new CanMapObservable[O, Observable] {
      def map[T, R](wrapped: O[T])(wrap: Observer[R] => Observer[T]): Observable[R] =
        new Observable.Wrapped(wrapped, wrap)
    }

  class Wrapped[T, R, O[X <: T] <: Observable[X]](wrapped: O[T], wrap: Observer[R] => Observer[T]) extends Observable[R] {

    def observe(observer: Observer[R]): Subscription = {
      val subscription = wrapped observe (wrap(observer))

      new Subscription {
        def unsubscribe() = subscription.unsubscribe()
      }
    }
  }

  trait Default[T] extends Observable[T] {
    private val observers = ListBuffer[Observer[T]]()

    def observe(observer: Observer[T]): Subscription = {
      observers += observer

      new Subscription {
        def unsubscribe() = observers -= observer
      }
    }

    protected def notify(information: T): Unit =
      observers foreach (_(information))
  }

  implicit class ObservableExtensions[O[T] <: Observable[T], T](val o: O[T]) extends AnyVal {

    def foreach(observer: Observer[T]): Subscription =
      o observe observer

    def collect[R, That[X] <: Observable[X]](f: PartialFunction[T, R])(
      implicit factory: CanMapObservable[O, That]): That[R] =
      factory.map(o) { observer =>
        value => if (f isDefinedAt value) observer(f(value))
      }

    def map[R, That[X] <: Observable[X]](f: T => R)(
      implicit factory: CanMapObservable[O, That]): That[R] =
      factory.map(o) { observer =>
        f andThen observer
      }

    def filter(f: T => Boolean)(
      implicit factory: CanMapObservable[O, O]): O[T] =
      factory.map(o) { observer =>
        value => if (f(value)) observer(value)
      }

  }

  implicit class Combinators1[O1[T] <: Observable[T], T](val o: O1[T]) {
    def |[X, R <: X, X1 >: T <: X, O2[X] <: Observable[X], That[X] <: Observable[X]](other: O2[R])(
      implicit factory: CanCombineObservable[O1, O2, That]): That[X] = 
        // the cast is completely safe here
        factory.combine(o.asInstanceOf[O1[X1]], other)
  }
}
