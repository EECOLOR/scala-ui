package ee.ui.members.detail

import ee.ui.members.ReadOnlyProperty
import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.PrependAux
import shapeless.::
import shapeless.HNil

import ee.util.Tuples._

abstract class CombinedPropertyBase[A <: Product, B, L <: HList, P <: HList, R <: Product](
  a: ReadOnlyProperty[A],
  b: ReadOnlyProperty[B])(implicit implicits: Implicits[A, B, L, P, R]) extends ReadOnlyProperty[R] {

  val defaultValue = a.defaultValue :+ b.defaultValue
  def value = a.value :+ b.value

  private val aChange = a.change map (_ :+ b.value)
  private val bChange = b.change map (a.value :+ _)
  private val aValueChange = a.valueChange map {
    case (oldValue, newValue) => (oldValue :+ b.value) -> (newValue :+ b.value)
  }
  private val bValueChange = b.valueChange map {
    case (oldValue, newValue) => (a.value :+ oldValue) -> (a.value :+ newValue)
  }

  val change = aChange | bChange
  val valueChange = aValueChange | bValueChange
}