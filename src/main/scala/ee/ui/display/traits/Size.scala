package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait ReadOnlyWidth {
  private[traits] val writableWidth = new Property(0d)
  def width: ReadOnlyProperty[Double] = writableWidth

}

trait ReadOnlyHeight {
  private[traits] val writableHeight = new Property(0d)
  def height: ReadOnlyProperty[Double] = writableHeight
}

trait Width extends ReadOnlyWidth {
  def width_=(value: Double)(implicit ev: AccessRestriction) = writableWidth.value = value
}

trait Height extends ReadOnlyHeight {
  def height_=(value: Double)(implicit ev: AccessRestriction) = writableHeight.value = value
}

trait ReadOnlySize extends ReadOnlyWidth with ReadOnlyHeight

trait Size extends ReadOnlySize with Width with Height

trait PartialExplicitSize

trait ExplicitWidth extends Width with PartialExplicitSize {
  def width_=(value: Double) = super.width_=(value)(RestrictedAccess)
}

trait ExplicitHeight extends Height with PartialExplicitSize {
  def height_=(value: Double) = super.height_=(value)(RestrictedAccess)
}

trait ExplicitSize extends Size with ExplicitWidth with ExplicitHeight

trait SizeProxy extends ExplicitSize {
  protected val target: ReadOnlySize

  override def width: Property[Double] = target.writableWidth
  override def width_=(value: Double) = target.writableWidth.value = value

  override def height: Property[Double] = target.writableHeight
  override def height_=(value: Double) = target.writableHeight.value = value
}
