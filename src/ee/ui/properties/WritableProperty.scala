package ee.ui.properties

trait WritableProperty[T] extends ReadOnlyProperty[T] {
    def value_=(value: T):Unit
    def set(value:T):Unit = this.value = value
}