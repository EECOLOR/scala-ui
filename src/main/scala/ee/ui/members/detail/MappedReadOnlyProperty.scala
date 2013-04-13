package ee.ui.members.detail

import ee.ui.members.ReadOnlyProperty

class MappedReadOnlyProperty[A, B](f: A => B, base: ReadOnlyProperty[A]) extends ReadOnlyProperty[B] {
  
  val defaultValue = f(base.defaultValue)
  
  def value = f(base.value)
  
  val change = base.change map f
  
  val valueChange = base.valueChange map {
    case (oldValue, newValue) => f(oldValue) -> f(newValue)
  }

  protected def value_=(value: B): Unit =
    throw new UnsupportedOperationException("The value_= method is not supported on a MappedReadOnlyProperty instance")
}