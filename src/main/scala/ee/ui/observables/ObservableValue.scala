package ee.ui.observables

import ee.ui.properties.Value
import ee.ui.events.ReadOnlyEvent
import scala.collection.generic.FilterMonadic
import scala.collection.generic.CanBuildFrom
import scala.language.higherKinds
import scala.language.reflectiveCalls
import ee.ui.bindings.BindableVariable
import ee.ui.properties.Property
import scala.language.implicitConversions

trait ObservableValue[T] extends Value[T] {
  def change: ReadOnlyEvent[T]
  def valueChange: ReadOnlyEvent[(T, T)]
}

object ObservableValue {

  implicit def observableToObservableValue[O[X] <: Observable[X], T](o: O[T]): ObservableValue[Option[T]] =
    new ObservableVariable[Option[T]] {
      val default = None
      o map Some[T] foreach setValue
    }

  implicit class Mappable[A](o: ObservableValue[A]) {

    def map[B](f: A => B): ObservableValue[B] = new ObservableValue[B] {
      def value = f(o.value)

      val change = o.change map f
      val valueChange = o.valueChange map {
        case (oldValue, newValue) => (f(oldValue), f(newValue))
      }
    }
  }

  object mapContents {
    // FMC = FilterMonadicContainer
    // D = Dummy
    implicit class FilterMonadicMappable[A, FMC[D] <: FilterMonadic[D, FMC[D]]](o: ObservableValue[FMC[A]]) {

      def map[B, That](f: A => B)(implicit bf: CanBuildFrom[FMC[A], B, That]): ObservableValue[That] = new ObservableValue[That] {
        def value = o.value.map(f)

        val change = o.change map (_ map f)
        val valueChange = o.valueChange map {
          case (oldValue, newValue) => (oldValue map f, newValue map f)
        }
      }
    }

    type HasMap[A, That[_]] = {
      def map[B](f: A => B): That[B]
    }

    // OVC = ObservableValueContainer
    // MC = MappableContainer
    // D = Dummy
    implicit class SimpleMappable[A, MC[D] <: HasMap[D, MC], OVC[D <: MC[_]] <: ObservableValue[D]](o: OVC[MC[A]]) {

      def map[B](f: A => B): ObservableValue[MC[B]] = new ObservableValue[MC[B]] {

        def value = o.value.map(f)

        val change = o.change map (_ map f)
        val valueChange = o.valueChange map {
          case (oldValue, newValue) => (oldValue map f, newValue map f)
        }
      }
    }
  }
}