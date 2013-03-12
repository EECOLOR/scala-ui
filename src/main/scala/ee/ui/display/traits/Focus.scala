package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

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