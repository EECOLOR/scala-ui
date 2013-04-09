package ee.util

import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.::
import shapeless.PrependAux
import shapeless.HNil
import scala.language.implicitConversions

object Tuples {

  /*
      We need some tools to help with the conversion

      hlister - Converts a tuple into an HList
      prepend - Can prepend one HList before another
      tupler  - Can convert an HList into a tuple
    */
  class Implicits[A <: Product, B, L <: HList, P <: HList, R <: Product](
    implicit val hlister: HListerAux[A, L],
    val prepend: PrependAux[L, B :: HNil, P],
    val tupler: TuplerAux[P, R])

  implicit def implicits[A <: Product, B, L <: HList, P <: HList, R <: Product](implicit hlister: HListerAux[A, L], prepend: PrependAux[L, B :: HNil, P], tupler: TuplerAux[P, R]) =
    new Implicits

  implicit class TupleOps[A <: Product](t: A) {

    /*
      Declare the method to append

      B - The type of element we want to append
      L - The HList representing the tuple A
      P - The HList after appending B
      R - The final result
    */
    def :+[B, L <: HList, P <: HList, R <: Product](b: B)(implicit implicits: Implicits[A, B, L, P, R]): R = {
      import implicits._
      // Let the helpers do their job
      tupler(prepend(hlister(t), b :: HNil))
    }

    /*
      The prepend method is similar to the append method but does not require
      the extra effort to append
    def +:[B, L <: HList, R <: Product](b: B)(
      // Here we use the :: type of shapeless
      implicit hlister: HListerAux[A, L], tupler: TuplerAux[B :: L, R]): R =
      tupler(b :: hlister(t))
     */
  }
}