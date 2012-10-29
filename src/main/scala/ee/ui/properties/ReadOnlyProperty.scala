package ee.ui.properties

trait ReadOnlyProperty[T] extends ObservableProperty[T] {
    def value:T
    def get:T = value
}

object ReadOnlyProperty {
	@inline implicit def propertyToValue[T](property:ReadOnlyProperty[T]):T = property.value
}