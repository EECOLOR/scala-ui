package ee.ui.members

import ee.ui.system.RestrictedAccess
import ee.ui.members.detail.BindingSource
import ee.ui.members.detail.BidirectionalBinding
import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.PrependAux
import shapeless.::
import shapeless.HNil
import scala.language.implicitConversions
import shapeless.HLister
import shapeless.Init
import shapeless.Last
import shapeless.InitAux
import shapeless.LastAux
import ee.ui.members.detail.CombinedPropertyBase

trait Property[T] extends ReadOnlyProperty[T] {

  def value_=(value: T): Unit

  def <==(source: BindingSource[T]) = source bindTo this

  def <==>(other: Property[T]) = BidirectionalBinding(this, other)
}

object Property {

  case class DefaultProperty[T](defaultValue: T) extends Property[T] {
    private var _value: T = defaultValue
    def value = _value
    def value_=(value: T): Unit =
      if (value != _value) {
        _value = value
        fireChange(value)
      }
  }

  def apply[T](defaultValue: T): Property[T] = DefaultProperty(defaultValue)

  def unapply[T](p: Property[T]) = Option(p) map (_.value)

  // Option extends Product so provide a shortcut to the SimpleCombinator
  implicit def optionCombinator[A <: Option[_]](a: Property[A]) = simpleCombinator(a)
  implicit def simpleCombinator[A](a: Property[A]) = {

    val f = Tuple1.apply[A] _

    val wrapped =
      new Property[Tuple1[A]] {
        val defaultValue = f(a.defaultValue)
        def value = f(a.value)
        def value_=(value: Tuple1[A]) = a.value = value._1
        override val change = a.change map f
      }
    new TupleCombinator(wrapped)
  }

  implicit class TupleCombinator[A <: Product](a: Property[A]) {

    import ee.util.Tuples._

    def |[B, L <: HList, P <: HList, R <: Product](b: Property[B])(
      implicit implicits:Implicits[A, B, L, P, R],
      reverseHLister: HListerAux[R, P], reverseTupler: TuplerAux[L, A],
      init: InitAux[P, L], last: LastAux[P, B]): Property[R] =

      new CombinedPropertyBase(a, b) with Property[R] {
        def value_=(value: R): Unit = {
          val valueAsHList = reverseHLister(value)
          a.value = reverseTupler(init(valueAsHList))
          b.value = last(valueAsHList)
        }
      }
  }

}