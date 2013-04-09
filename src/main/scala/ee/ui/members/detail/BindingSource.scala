package ee.ui.members.detail

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import scala.language.implicitConversions
import ee.ui.members.ReadOnlyEvent
import shapeless.TuplerAux
import shapeless.HListerAux
import shapeless.HList
import shapeless.PrependAux
import shapeless.::
import shapeless.HNil

class BindingSource[A](protected val source: ReadOnlyProperty[A]) { self =>

  def bindTo(property: Property[A]): Unit =
    bindWith(property.value_=)

  def bindWith(method: A => Unit): Unit = {
    method(source.value)

    source change method
  }

  def filter(f: A => Boolean): BindingSource[A] =
    new BindingSource[A](self.source) {
      override def bindWith(method: A => Unit) =
        super.bindWith { value =>
          if (f(value)) method(value)
        }
    }
}

object BindingSource {

}
