package ee.util

import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.::
import shapeless.PrependAux
import shapeless.HNil

object Tuples {
  implicit class TupleOps[A <: Product](t: A) {

    /*
    Declare the method to append

    B - The type of element we want to append
    L - The HList representing the tuple A
    P - The HList after appending B
    R - The final result
  */
    def :+[B, L <: HList, P <: HList, R <: Product](b: B)(
      /*
      We need some tools to help with the conversion

      hlister - Converts a tuple into an HList
      prepend - Can prepend one HList before another
      tupler  - Can convert an HList into a tuple
      */
      implicit hlister: HListerAux[A, L], prepend: PrependAux[L, B :: HNil, P], tupler: TuplerAux[P, R]): R =
      // Let the helpers do their job
      tupler(prepend(hlister(t), b :: HNil))

    /*
    The prepend method is similar to the append method but does not require
    the extra effort to append
  */
    def +:[B, L <: HList, R <: Product](b: B)(
      // Here we use the :: type of shapeless
      implicit hlister: HListerAux[A, L], tupler: TuplerAux[B :: L, R]): R =
      tupler(b :: hlister(t))
  }
}