package ee.ui.members.detail

import ee.ui.members.Property
import ee.ui.members.Event
import ee.util.TupleAppendOps

class TupleCombinator[A](a: Property[A]) extends ReadOnlyTupleCombinator(a) {

  def |[B, C](b: Property[B])(implicit tupleOps: TupleAppendOps[A, B, C]): Property[C] =

    new CombinedPropertyBase(a, b) with Property[C] {

      val changeSubscription = changeEvent apply change.fire
      val valueChangeSubscription = valueChangeEvent apply valueChange.fire

      val change = Event[C]
      val valueChange = Event[(C, C)]

      import tupleOps._

      def setValue(value: C): Unit = {
        changeSubscription.disable()
        valueChangeSubscription.disable()
        a.value = value.init
        b.value = value.last
        changeSubscription.enable()
        valueChangeSubscription.enable()
      }
    }
}