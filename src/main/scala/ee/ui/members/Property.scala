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
import ee.ui.members.detail.MappedProperty

trait Property[T] extends ReadOnlyProperty[T] {

  def value_=(value: T): Unit = {
    val oldValue = this.value
    if (value != oldValue) {
      setValue(value)
      fireEvents(oldValue, value)
    }
  }

  protected def setValue(value: T): Unit

  protected def fireEvents(oldValue: T, newValue: T): Unit = {
    fireChange(value)
    fireValueChange(oldValue, newValue)
  }

  def <==(source: BindingSource[T]) = source bindTo this

  def <==>(other: Property[T]) = BidirectionalBinding(this, other)
}

object Property {

  case class DefaultProperty[T](defaultValue: T) extends Property[T] {
    private var _value: T = defaultValue
    def value = _value
    def setValue(value: T): Unit = _value = value
    val change = ReadOnlyEvent[T]
    val valueChange = ReadOnlyEvent[(T, T)]
  }

  def apply[T](defaultValue: T): Property[T] = DefaultProperty(defaultValue)

  def unapply[T](p: Property[T]) = Option(p) map (_.value)

  // Option extends Product so provide a shortcut to the SimpleCombinator
  implicit def optionCombinator[A <: Option[_]](a: Property[A]) = simpleCombinator(a)
  implicit def simpleCombinator[A](a: Property[A]): TupleCombinator[Tuple1[A]] = {

    val f = Tuple1.apply[A] _

    def r(t: Tuple1[A]) = t._1

    val wrapped = new MappedProperty[A, Tuple1[A]](f, r, a)

    new TupleCombinator(wrapped)
  }

  implicit class TupleCombinator[A <: Product](a: Property[A]) {

    import ee.util.Tuples._

    def |[B, L <: HList, P <: HList, R <: Product](b: Property[B])(
      implicit implicits: Implicits[A, B, L, P, R],
      reverseHLister: HListerAux[R, P], reverseTupler: TuplerAux[L, A],
      init: InitAux[P, L], last: LastAux[P, B]): Property[R] =

      new CombinedPropertyBase(a, b) with Property[R] {

        /* 
         * Do not fire events, we set the value on the sub elements, that will
         * trigger the events to fire in CombinedPropertyBase
         */  
        override protected def fireEvents(oldValue: R, newValue: R): Unit = {}

        def setValue(value: R): Unit = {
          val valueAsHList = reverseHLister(value)
          a.value = reverseTupler(init(valueAsHList))
          b.value = last(valueAsHList)
        }
      }
  }

}