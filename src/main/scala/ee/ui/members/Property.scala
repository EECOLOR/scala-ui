package ee.ui.members

import ee.ui.members.detail.BidirectionalBinding
import ee.ui.members.detail.BindingSource
import ee.ui.members.detail.MappedProperty
import ee.ui.members.detail.TupleCombinator
import scala.language.implicitConversions
import ee.ui.primitives.Bounds
import ee.util.TupleAppendOps

trait Property[A] extends ReadOnlyProperty[A] {

  def value_=(value: A): Unit = {
    val oldValue = this.value
    if (value != oldValue) {
      setValue(value)
      fireEvents(oldValue, value)
    }
  }

  protected def setValue(value: A): Unit

  def <==[B <: A](source: BindingSource[B]) = source bindTo this

  def <==>(other: Property[A]) = BidirectionalBinding(this, other)
}

trait PropertyLowerPriorityImplicits {
  implicit def simpleCombinator[A](a: Property[A]): TupleCombinator[Tuple1[A]] = {

    // map
    val f = Tuple1.apply[A] _
    // reverse map
    def r(t: Tuple1[A]) = t._1
    // wrap the property to become a product
    val wrapped: Property[Tuple1[A]] = new MappedProperty[A, Tuple1[A]](f, r, a)

    new TupleCombinator(wrapped)
  }
}

object Property extends PropertyLowerPriorityImplicits {

  case class DefaultProperty[T](defaultValue: T) extends Property[T] {
    private var _value: T = defaultValue
    def value = _value
    def setValue(value: T): Unit = _value = value
    val change = ReadOnlyEvent[T]
    val valueChange = ReadOnlyEvent[(T, T)]
  }

  def apply[T](defaultValue: T): Property[T] = DefaultProperty(defaultValue)

  def unapply[T](p: Property[T]) = Option(p) map (_.value)

  implicit def tupleCombinator[A](a: Property[A])(implicit ev: TupleAppendOps[A, _, _]) = new TupleCombinator(a)
}