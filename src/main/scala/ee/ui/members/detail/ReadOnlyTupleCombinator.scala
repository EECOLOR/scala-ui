package ee.ui.members.detail

import ee.ui.members.ReadOnlyProperty
import ee.util.TupleAppendOps

class ReadOnlyTupleCombinator[A](a: ReadOnlyProperty[A]) {

  def |[B, C](b: ReadOnlyProperty[B])(implicit tupleOps: TupleAppendOps[A, B, C]): ReadOnlyProperty[C] =

    new CombinedPropertyBase(a, b) {
      val change = changeEvent
      val valueChange = valueChangeEvent

      protected def value_=(value: C): Unit =
        throw new UnsupportedOperationException("The value_= method is not supported on a combined instance")
    }
}