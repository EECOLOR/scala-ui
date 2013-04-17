package ee.ui.members.detail

import ee.ui.members.ReadOnlyProperty
import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.PrependAux
import shapeless.::
import shapeless.HNil

import ee.util.Tuples._

abstract class CombinedPropertyBase[A, B, C](
  a: ReadOnlyProperty[A],
  b: ReadOnlyProperty[B])(implicit tupleOps:TupleOps[A, B, C]) extends ReadOnlyProperty[C] {

  import tupleOps._
  
  val defaultValue = a.defaultValue :+ b.defaultValue
  def value = a.value :+ b.value

  val changeEvent = {
    val aChange = a.change map (_ :+ b.value)
    val bChange = b.change map (a.value :+ _)

    aChange | bChange
  }

  val valueChangeEvent = {
    val aValueChange = a.valueChange map {
      case (oldValue, newValue) => (oldValue :+ b.value) -> (newValue :+ b.value)
    }
    val bValueChange = b.valueChange map {
      case (oldValue, newValue) => (a.value :+ oldValue) -> (a.value :+ newValue)
    }

    aValueChange | bValueChange
  }
}