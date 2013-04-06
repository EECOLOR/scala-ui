package ee.ui.members

import ee.ui.system.AccessRestriction
import scala.language.implicitConversions
import ee.ui.members.detail.BindingSource
import ee.ui.system.RestrictedAccess
import shapeless.HList
import shapeless.HListerAux
import shapeless.TuplerAux
import shapeless.PrependAux
import shapeless.::
import shapeless.HNil

trait ReadOnlyProperty[T] { self =>
  val defaultValue: T
  def value: T
  protected def value_=(value: T): Unit
  val change = ReadOnlyEvent[T]()

  def map[R](f: T => R): ReadOnlyProperty[R] =
    new ReadOnlyProperty[R] {
      val defaultValue = f(self.defaultValue)
      def value = f(self.value)
      override val change = self.change map f

      protected def value_=(value: R): Unit =
        throw new UnsupportedOperationException("The value_= method is not supported on a mapped instance")
    }

}

object ReadOnlyProperty {
  def apply[T](defaultValue: T): ReadOnlyProperty[T] = new Property(defaultValue)

  def setValue[T](readOnlyProperty: ReadOnlyProperty[T], value: T)(implicit ev: AccessRestriction) =
    readOnlyProperty.value = value

  implicit def propertyToValue[T](p: ReadOnlyProperty[T]): T = p.value

  def unapply[T](r: ReadOnlyProperty[T]): Option[T] =
    Option(r) map (_.value)

  implicit def fromReadOnlyEvent[T](event: ReadOnlyEvent[T]): ReadOnlyProperty[Option[T]] =
    new ReadOnlyProperty[Option[T]] {
      val defaultValue = None
      def value = defaultValue
      protected def value_=(value: Option[T]) =
        throw new UnsupportedOperationException("The value_= method is not supported on a mapped instance")

      override val change = event map Option.apply
    }

  implicit def toBindingSource[T](source: ReadOnlyProperty[T]) =
    new BindingSource[T](source) {
      def bindTo(property: Property[T]) = {
        property.value = source.value

        source.change { property.value = _ }
      }
    }

  implicit class SimpleCombinator[A](a: ReadOnlyProperty[A]) {

    def |[B](b: ReadOnlyProperty[B]): ReadOnlyProperty[(A, B)] =

      new ReadOnlyProperty[(A, B)] {
        val defaultValue = (a.defaultValue, b.defaultValue)
        def value = (a.value, b.value)
        override val change = ReadOnlyEvent[(A, B)]()

        private def fireChange(value: (A, B)): Unit =
          ReadOnlyEvent.fire(change, value)(RestrictedAccess)

        a.change map (_ -> b.value) apply fireChange
        b.change map (a.value -> _) apply fireChange

        protected def value_=(value: (A, B)): Unit =
          throw new UnsupportedOperationException("The value_= method is not supported on a combined instance")
      }
  }

  // Option extends Product so provide a shortcut to the SimpleCombinator
  implicit def optionCombinator[A <: Option[_]](a: ReadOnlyProperty[A]) = new SimpleCombinator(a)
  
  implicit class TupleCombinator[A <: Product](a: ReadOnlyProperty[A]) {

    import ee.util.Tuples._

    def |[B, L <: HList, P <: HList, R <: Product](b: ReadOnlyProperty[B])(
      implicit hlister: HListerAux[A, L], prepend: PrependAux[L, B :: HNil, P], tupler: TuplerAux[P, R]): ReadOnlyProperty[R] =

      new ReadOnlyProperty[R] {
        val defaultValue = a.defaultValue :+ b.defaultValue
        def value = a.value :+ b.value
        override val change = ReadOnlyEvent[R]()

        private def fireChange(value: R): Unit =
          ReadOnlyEvent.fire(change, value)(RestrictedAccess)

        a.change map (_ :+ b.value) apply fireChange
        b.change map (a.value :+ _) apply fireChange

        protected def value_=(value: R): Unit =
          throw new UnsupportedOperationException("The value_= method is not supported on a combined instance")
      }
  }
}