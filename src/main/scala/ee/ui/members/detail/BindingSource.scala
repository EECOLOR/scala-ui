package ee.ui.members.detail

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty

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
