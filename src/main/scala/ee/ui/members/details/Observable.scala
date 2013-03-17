package ee.ui.members.details

import scala.collection.mutable.ListBuffer
import scala.language.higherKinds
import scala.language.implicitConversions
import ee.ui.events.Observer
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait Observable[T] {
  def observe(observer: Observer[T]): Subscription
}

object Observable {

  implicit def defaultTyper[O[~] <: Observable[~]] =
    new CanTypeObservable[O, Observable] {
      def typed[T](observable: Observable[T]): Observable[T] = observable
    }

  class Proxy[T, O[~ <: T] <: Observable[~]](source: O[T]) extends Observable[T] {
    def observe(observer: Observer[T]) = source observe observer
  }

  class NotifyableProxy[T, O[~ <: T] <: Observable[~] with Notifyable[~]](source: O[T]) extends Proxy(source) with Notifyable[T] {
    def notify(information: T) = Notifyable.notify(source, information)(RestrictedAccess)
  }

  def wrapped[T, R, O[~] <: Observable[~]](observable: O[T])(f: Observer[R] => Observer[T]): Observable[R] =
    new Wrapped(observable, f)

  class Wrapped[T, R, O[~ <: T] <: Observable[~]](
    wrapped: O[T], wrap: Observer[R] => Observer[T]) extends Observable[R] {

    def observe(observer: Observer[R]): Subscription =
      wrapped observe (wrap(observer))
  }

  trait Default[T] extends Observable[T] with Notifyable[T] {
    private val observers = ListBuffer[Observer[T]]()

    def observe(observer: Observer[T]): Subscription = {
      observers += observer

      new Subscription {
        def unsubscribe() = observers -= observer
      }
    }

    def notify(information: T): Unit =
      observers foreach (_(information))
  }

  class Single[T, O[~ <: T] <: Observable[~]](source: O[T]) extends Proxy(source) {

    override def observe(observer: Observer[T]): Subscription = {
      var subscription: Option[Subscription] = None

      def selfUnsubscribingOverver(value: T): Unit = {
        subscription.foreach(_.unsubscribe())
        observer(value)
      }

      val actualSubscription = source observe selfUnsubscribingOverver
      subscription = Some(actualSubscription)
      actualSubscription
    }
  }

  implicit class ObservableExtensions[O[~] <: Observable[~], T](val o: O[T]) {

    def foreach(observer: Observer[T]): Subscription =
      o observe observer

    def collect[R, That[~] <: Observable[~]](f: PartialFunction[T, R])(
      implicit typer: CanTypeObservable[O, That]): That[R] = {

      val observable =
        wrapped(o) { observer: Observer[R] =>
          value => if (f isDefinedAt value) observer(f(value))
        }

      typer typed observable
    }

    def map[R, That[~] <: Observable[~]](f: T => R)(
      implicit typer: CanTypeObservable[O, That]): That[R] = {

      val observable =
        wrapped(o) { observer: Observer[R] =>
          f andThen observer
        }

      typer typed observable
    }

    def filter[That[~] <: Observable[~]](f: T => Boolean)(
      implicit typer: CanTypeObservable[O, That]): That[T] = {
      val observable =
        wrapped(o) { observer: Observer[T] =>
          value => if (f(value)) observer(value)
        }

      typer typed observable
    }

    def once[That[~] <: Observable[~]](implicit typer: CanTypeObservable[O, That]): That[T] =
      typer.typed(new Single(o))

  }

  implicit class Combinators1[O1[~] <: Observable[~], T](val o: O1[T]) {
    def |[X, R <: X, T_ >: T <: X, O2[~] <: Observable[~], That[~] <: Observable[~]](other: O2[R])(
      implicit typer: CanTypeObservable[O1, That]): That[X] = {

      val observable =
        new Observable.Default[X] {
          // the cast is completely safe here
          o.asInstanceOf[O1[T_]] observe notify
          other observe notify
        }
      
      typer typed observable
    }
  }
}
