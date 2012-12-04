package ee.ui.traits

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty

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
