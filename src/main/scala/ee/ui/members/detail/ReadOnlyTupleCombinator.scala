package ee.ui.members.detail

import ee.ui.members.ReadOnlyProperty
import ee.util.Tuples.Implicits
import shapeless.HList

class ReadOnlyTupleCombinator[A <: Product](a: ReadOnlyProperty[A]) {

  import ee.util.Tuples._

  def |[B, L <: HList, P <: HList, R <: Product](b: ReadOnlyProperty[B])(
    implicit implicits: Implicits[A, B, L, P, R]): ReadOnlyProperty[R] = {

    new CombinedPropertyBase(a, b) {
      val change = changeEvent
      val valueChange = valueChangeEvent

      protected def value_=(value: R): Unit =
        throw new UnsupportedOperationException("The value_= method is not supported on a combined instance")
    }
  }
}