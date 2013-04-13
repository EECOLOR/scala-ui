package ee.ui.members.detail

import ee.ui.members.Property

class MappedProperty[A, B](f: A => B, r: B => A, base: Property[A]) extends MappedReadOnlyProperty(f, base) with Property[B] {

  override def value_=(value: B) =
    super[Property].value = value

  //Do not fire the events, they will be fired by the MappedReadOnlyProperty when the base value changes
  override def fireEvents(oldValue: B, newValue: B): Unit = {}

  protected def setValue(value: B): Unit =
    base.value = r(value)

}