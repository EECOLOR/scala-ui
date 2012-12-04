package ee.ui.traits

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty

trait ReadOnlyFocus {
  private[traits] val writableFocused = new Property(false)
  def focused: ReadOnlyProperty[Boolean] = writableFocused
}

trait Focus extends ReadOnlyFocus {
  def focused_=(value: Boolean)(implicit ev: AccessRestriction) = writableFocused.value = value
}

trait ExplicitFocus extends Focus {
  def focused_=(value: Boolean) = super.focused_=(value)(RestrictedAccess)
}

trait FocusProxy extends ExplicitFocus {
  val target: ReadOnlyFocus
  
  override def focused: Property[Boolean] = target.writableFocused
  override def focused_=(value: Boolean) = target.writableFocused.value = value
}