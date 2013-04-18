package ee.ui.members

import ee.ui.system.AccessRestriction
import scala.language.implicitConversions
import ee.ui.members.detail.BindingSource
import ee.ui.system.RestrictedAccess
import ee.ui.members.detail.CombinedPropertyBase
import ee.ui.members.detail.MappedReadOnlyProperty
import ee.ui.members.detail.ReadOnlyTupleCombinator
import ee.util.TupleAppendOps

trait ReadOnlyProperty[T] { self =>
  val defaultValue: T
  def value: T
  protected def value_=(value: T): Unit
  val change: ReadOnlyEvent[T]
  val valueChange: ReadOnlyEvent[(T, T)]

  protected def fireChange(value: T) =
    ReadOnlyEvent.fire(change, value)(RestrictedAccess)

  protected def fireValueChange(change: (T, T)) =
    ReadOnlyEvent.fire(valueChange, change)(RestrictedAccess)

  protected def fireEvents(oldValue: T, newValue: T): Unit = {
    fireChange(value)
    fireValueChange(oldValue -> newValue)
  }

  def map[R](f: T => R): ReadOnlyProperty[R] =
    new MappedReadOnlyProperty[T, R](f, this)
}

trait ReadOnlyPropertyLowerPriorityImplicits {
  implicit def simpleCombinator[A](a: ReadOnlyProperty[A]) = new ReadOnlyTupleCombinator(a map Tuple1.apply)
}

object ReadOnlyProperty extends ReadOnlyPropertyLowerPriorityImplicits {
  def apply[T](defaultValue: T): ReadOnlyProperty[T] = Property(defaultValue)

  def setValue[T](readOnlyProperty: ReadOnlyProperty[T], value: T)(implicit ev: AccessRestriction) =
    readOnlyProperty.value = value

  implicit def propertyToValue[T](p: ReadOnlyProperty[T]): T = p.value

  def unapply[T](r: ReadOnlyProperty[T]): Option[T] =
    Option(r) map (_.value)

  implicit def fromReadOnlyEvent[A, B >: A](event: ReadOnlyEvent[A]): ReadOnlyProperty[Option[B]] = {
    val property = Property[Option[B]](None)
    val mappedEvent = event map Option.apply
    mappedEvent apply property.value_=

    property
  }

  implicit def toBindingSource[A](source: ReadOnlyProperty[A]): BindingSource[A] =
    new BindingSource(source)

  implicit def tupleCombinator[A](a: ReadOnlyProperty[A])(implicit ev: TupleAppendOps[A, _, _]) =
    new ReadOnlyTupleCombinator(a)
}