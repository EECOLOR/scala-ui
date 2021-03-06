package ee.ui.members.details

import scala.collection.generic.CanBuildFrom
import scala.collection.generic.FilterMonadic

import ee.ui.members.ReadOnlyEvent
import ee.util.tuples.TupleOps
import shapeless.::
import shapeless.HList
import shapeless.HListerAux
import shapeless.HNil
import shapeless.PrependAux
import shapeless.TuplerAux

import scala.language.implicitConversions
import scala.language.higherKinds
import scala.language.reflectiveCalls

trait ObservableValue[T] extends Value[T] {
  def change: ReadOnlyEvent[T]
  def valueChange: ReadOnlyEvent[(T, T)]
}

trait LowerPriorityObservableValueImplicits {
  implicit class SimpleCombinator[A](a: ObservableValue[A]) {
    def |[B](b: ObservableValue[B]): ObservableValue[(A, B)] =
      new ObservableValue[(A, B)] {
        def value = (a.value, b.value)
        val change: ReadOnlyEvent[(A, B)] =
          new Observable.Default[(A, B)] with ReadOnlyEvent[(A, B)] {
            val aEvent = a.change map { (_, b.value) }
            val bEvent = b.change map { (a.value, _) }

            (aEvent | bEvent) observe notify
          }

        val valueChange: ReadOnlyEvent[((A, B), (A, B))] =
          new Observable.Default[((A, B), (A, B))] with ReadOnlyEvent[((A, B), (A, B))] {
            val aEvent = a.valueChange map {
              case (oldValue, newValue) => ((oldValue, b.value), (newValue, b.value))
            }
            val bEvent = b.valueChange map {
              case (oldValue, newValue) => ((a.value, oldValue), (a.value, newValue))
            }

            (aEvent | bEvent) observe notify
          }
      }
  }
  
  implicit def toBindingSource[T, O[~] <: ObservableValue[~]](o:O[T]):BindingSource[O, T] = 
    BindingSource(o)
}

object ObservableValue extends LowerPriorityObservableValueImplicits {

  implicit def observableToObservableValue[T, O[~] <: Observable[~]](o: O[T]): ObservableValue[Option[T]] =
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
    implicit class FilterMonadicMappable[A, FMC[~] <: FilterMonadic[~, FMC[~]]](o: ObservableValue[FMC[A]]) {

      def map[B, That[~]](f: A => B)(implicit bf: CanBuildFrom[FMC[A], B, That[B]]): ObservableValue[That[B]] =
        new ObservableValue[That[B]] {
          def value = o.value.map(f)

          val change = o.change map (_ map f)
          val valueChange = o.valueChange map {
            case (oldValue, newValue) => (oldValue map f, newValue map f)
          }
        }
    }

    type HasMap[A, That[~]] = {
      def map[B](f: A => B): That[B]
    }

    // OVC = ObservableValueContainer
    // MC = MappableContainer
    // D = Dummy
    implicit class SimpleMappable[A, MC[~] <: HasMap[~, MC], OVC[~ <: MC[_]] <: ObservableValue[~]](o: OVC[MC[A]]) {

      def map[B](f: A => B): ObservableValue[MC[B]] =
        new ObservableValue[MC[B]] {

          def value = o.value.map(f)

          val change = o.change map (_ map f)
          val valueChange = o.valueChange map {
            case (oldValue, newValue) => (oldValue map f, newValue map f)
          }
        }
    }
  }

  // Apparently Option extends Product... so provide a shortcut to the SimpleCombinator
  implicit def optionCombinator[A <: Option[_]](a: ObservableValue[A]) = new SimpleCombinator(a)

  implicit class TupleCombinator[A <: Product](a: ObservableValue[A]) {

    import ee.util.tuples._

    def |[B, L <: HList, P <: HList, R <: Product](b: ObservableValue[B])(
      implicit hlister: HListerAux[A, L], prepend: PrependAux[L, B :: HNil, P], tupler: TuplerAux[P, R]): ObservableValue[R] = {

      new ObservableValue[R] {
        def value = a.value :+ b.value

        val change: ReadOnlyEvent[R] =
          new Observable.Default[R] with ReadOnlyEvent[R] {
            val aEvent = a.change map { a => a :+ b.value }
            val bEvent = b.change map { b => a.value :+ b }

            (aEvent | bEvent) observe notify
          }

        val valueChange: ReadOnlyEvent[(R, R)] =
          new Observable.Default[(R, R)] with ReadOnlyEvent[(R, R)] {
            val aEvent = a.valueChange map {
              case (oldValue, newValue) => (oldValue :+ b.value, newValue :+ b.value)
            }
            val bEvent = b.valueChange map {
              case (oldValue, newValue) => (a.value :+ oldValue, a.value :+ newValue)
            }

            (aEvent | bEvent) observe notify
          }
      }
    }
  }
}