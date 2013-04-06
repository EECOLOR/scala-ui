package ee.ui.members

import ee.ui.system.RestrictedAccess
import ee.ui.members.detail.BindingSource

case class Property[T](defaultValue: T) extends ReadOnlyProperty[T] {
  private var _value: T = defaultValue
  def value = _value
  def value_=(value: T): Unit =
    if (value != _value) {
      _value = value
      ReadOnlyEvent.fire(change, value)(RestrictedAccess)
    }

  def <==(source: BindingSource[T]) = source bindTo this
}
