package ee.ui.properties

trait Bindable[T] extends WritableProperty[T] {
    
    def bindTo(property: ReadOnlyProperty[T]): Unit = {
        property onChange { (o, n, p) =>
            value = n
            true
        }
        value = property.value
    }
    
    def ==>(property: Bindable[T]):Unit = property bindTo this 
    def <== = bindTo _
}