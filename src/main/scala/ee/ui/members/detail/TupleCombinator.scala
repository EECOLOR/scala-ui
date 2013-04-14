package ee.ui.members.detail

import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.LastAux
import shapeless.InitAux
import ee.ui.members.Property
import ee.ui.members.Event

class TupleCombinator[A <: Product](a: Property[A]) {

  import ee.util.Tuples._

  def |[B, L <: HList, P <: HList, R <: Product](b: Property[B])(
    implicit implicits: Implicits[A, B, L, P, R],
    reverseHLister: HListerAux[R, P], reverseTupler: TuplerAux[L, A],
    init: InitAux[P, L], last: LastAux[P, B]): Property[R] =

    new CombinedPropertyBase(a, b) with Property[R] {

      val changeSubscription = changeEvent apply change.fire
      val valueChangeSubscription = valueChangeEvent apply valueChange.fire

      val change = Event[R]
      val valueChange = Event[(R, R)]

      def setValue(value: R): Unit = {
        val valueAsHList = reverseHLister(value)
        changeSubscription.disable()
        valueChangeSubscription.disable()
        a.value = reverseTupler(init(valueAsHList))
        b.value = last(valueAsHList)
        changeSubscription.enable()
        valueChangeSubscription.enable()
      }
    }
}