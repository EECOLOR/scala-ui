package ee.ui.properties

trait ReadOnlyProperty[T] extends Observable[T] {
    def value:T
    def get:T = value
}

object ReadOnlyProperty {
	implicit def propertyToValue[T](property:Property[T]):T = property.value
}