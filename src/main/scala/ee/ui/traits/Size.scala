package ee.ui.traits

import ee.ui.properties.Property

trait Width {
  private val _width = new Property(0d)
  def width = _width
  def width_=(value: Double) = width.value = value
}

trait Height {
  private val _height = new Property(0d)
  def height = _height
  def height_=(value: Double) = height.value = value
}

trait Size extends Width with Height

trait LayoutWidth extends Width {
	def width_=(value: Double)(implicit ev: AccessRestriction) = super.width_=(value)
}

trait LayoutHeight extends Height {
	def height_=(value: Double)(implicit ev: AccessRestriction) = super.height_=(value)
}

trait LayoutSize extends Size with LayoutWidth with LayoutHeight

trait PartialExplicitSize

trait ExplicitWidth extends LayoutWidth with PartialExplicitSize {
	override def width_=(value: Double) = super.width_=(value)(RestrictedAccess)
}

trait ExplicitHeight extends LayoutHeight with PartialExplicitSize {
	override def height_=(value: Double) = super.height_=(value)(RestrictedAccess)
}

trait ExplicitSize extends LayoutSize with ExplicitWidth with ExplicitHeight 
