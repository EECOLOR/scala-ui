package ee.ui.display.traits

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.system.AccessRestriction
import ee.ui.system.RestrictedAccess

trait ReadOnlyPosition {
	private[traits] val writableX = new Property(0d)
	def x: ReadOnlyProperty[Double] = writableX
  
	private[traits] val writableY = new Property(0d)
	def y: ReadOnlyProperty[Double] = writableY
}

trait Position extends ReadOnlyPosition {
  def x_=(value: Double)(implicit ev: AccessRestriction) = writableX.value = value

  def y_=(value: Double)(implicit ev: AccessRestriction) = writableY.value = value
}

trait ExplicitPosition extends Position {
  def x_=(value: Double) = super.x_=(value)(RestrictedAccess)
  def y_=(value: Double) = super.y_=(value)(RestrictedAccess)
}

trait PositionProxy extends ExplicitPosition {
  val target:ReadOnlyPosition
  
  override def x: Property[Double] = target.writableX
  override def x_=(value: Double) = target.writableX.value = value
  
  override def y: Property[Double] = target.writableX
  override def y_=(value: Double) = target.writableY.value = value
}