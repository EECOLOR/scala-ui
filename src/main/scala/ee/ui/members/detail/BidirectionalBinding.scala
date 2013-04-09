package ee.ui.members.detail

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess
import ee.ui.members.ReadOnlyEvent

class BidirectionalBinding[T](internalLeft: Property[T], internalRight: Property[T]) {

  internalRight.value = internalLeft.value

  private def changeHandler(subscription: Subscription, property: PropertyProxy[T])(value: T) = {
    subscription.disable()
    property.value = value
    subscription.enable()
  }

  val leftSubscription: Subscription =
    internalLeft change changeHandler(rightSubscription, rightRepresentation)

  val rightSubscription: Subscription =
    internalRight change changeHandler(leftSubscription, leftRepresentation)

  case class PropertyProxy[T](source: Property[T]) extends ReadOnlyProperty[T] {
    val defaultValue = source.defaultValue
    def value = source.value
    def value_=(value: T) = {
      source.value = value
      fireChange(value)
    }
  }

  val leftRepresentation = PropertyProxy(internalLeft)
  val rightRepresentation = PropertyProxy(internalRight)

  val left: ReadOnlyProperty[T] = leftRepresentation
  val right: ReadOnlyProperty[T] = rightRepresentation

}

object BidirectionalBinding {
  def apply[T](left: Property[T], right: Property[T]) = new BidirectionalBinding(left, right)
}