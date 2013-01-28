package ee.ui.observable

import scala.language.implicitConversions

trait ObservableValue[T] extends Observable[T] { self =>
  def value: T
}

trait LowLevelImplicits {
  @inline implicit def observableValueToValue[T](property: ObservableValue[T]): T = property.value
}

object ObservableValue extends LowLevelImplicits {
  implicit def observableToOptionalObservableValue[T](o: Observable[T]): ObservableValue[Option[T]] =
    new ObservableValue[Option[T]] with WrappedSubscription {
      val subscription =
        o foreach { value =>
          notify(Some(value))
        }

      def value = None
    }
  
  implicit class ObservableValueExtension[T](o: ObservableValue[T]) {

    def map[R](f: T => R): ObservableValue[R] with Subscription =
      new ObservableValue[R] with WrappedSubscription {
        val subscription =
          o foreach { value =>
            notify(f(value))
          }
        
        def value = f(o.value)
      }
  }
}