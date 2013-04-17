package ee.ui.members.detail

import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.LastAux
import shapeless.InitAux
import ee.ui.members.Property
import ee.ui.members.Event
import ee.util.Tuples

class TupleCombinator[A](a: Property[A]) extends ReadOnlyTupleCombinator(a) {

  def |[B, C](b: Property[B])(
    implicit tupleOps: Tuples.TupleOps[A, B, C]): Property[C] =

    new CombinedPropertyBase(a, b) with Property[C] {

      val changeSubscription = changeEvent apply change.fire
      val valueChangeSubscription = valueChangeEvent apply valueChange.fire

      val change = Event[C]
      val valueChange = Event[(C, C)]

      def setValue(value: C): Unit = {
        changeSubscription.disable()
        valueChangeSubscription.disable()
        a.value = tupleOps.init(value)
        b.value = tupleOps.last(value)
        changeSubscription.enable()
        valueChangeSubscription.enable()
      }
    }
}