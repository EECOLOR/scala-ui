package ee.ui.properties

trait Bindable[T] extends WritableProperty[T] {

  def bindTo(binding:Binding[T]):Unit = binding bind {value = _}
  
  def ==>(property: Bindable[T]): Unit = property bindTo this
  def <== = bindTo _

}

class Binding[T](property: ReadOnlyProperty[T]) {
  def bind(applyValue: T => Unit) = {
	  property forNewValue applyValue
	  applyValue(property.value)
  }
  
  def when(condition: T => Boolean) = new ConditionalBinding(property, condition)
}

object Binding {
  implicit def readOnlyPropertyToBinding[T](property:ReadOnlyProperty[T]):Binding[T] = new Binding(property)
}

class ConditionalBinding[T](property: ReadOnlyProperty[T], condition: T => Boolean) extends Binding[T](property) {

  override def bind(applyValue: T => Unit) = {
    super.bind(n => if (condition(n)) applyValue(n))
  }

    
}
