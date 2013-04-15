package ee.ui.members

import ee.ui.members.detail.BidirectionalBinding
import ee.ui.members.detail.BindingSource
import ee.ui.members.detail.MappedProperty
import ee.ui.members.detail.TupleCombinator

import scala.language.implicitConversions

trait Property[A] extends ReadOnlyProperty[A] {

  def value_=(value: A): Unit = {
    val oldValue = this.value
    if (value != oldValue) {
      setValue(value)
      fireEvents(oldValue, value)
    }
  }

  protected def setValue(value: A): Unit

  protected def fireEvents(oldValue: A, newValue: A): Unit = {
    fireChange(value)
    fireValueChange(oldValue -> newValue)
  }

  def <==[B <: A](source: BindingSource[B]) = source bindTo this

  def <==>(other: Property[A]) = BidirectionalBinding(this, other)
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

    // map
    val f = Tuple1.apply[A] _
    // reverse map
    def r(t: Tuple1[A]) = t._1
    // wrap the property to become a product
    val wrapped = new MappedProperty[A, Tuple1[A]](f, r, a)
    
    tupleCombinator(wrapped)
  }
  
  implicit def tupleCombinator[A <: Product](a:Property[A]) = new TupleCombinator(a)
}