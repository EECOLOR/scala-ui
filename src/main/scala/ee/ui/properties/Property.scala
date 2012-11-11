package ee.ui.properties

class Property[T](default: T) extends Bindable[T] with Equals {

  private var _value = default
  def value = _value
  def value_=(value: T) =
    if (_value != value) valueChange(_value, value) {
      _value = value
    }

  def reset = value = default
  def isDefault = value == default
  def isChanged = value != default

  def canEqual(other: Any) = {
    other.isInstanceOf[Property[T]]
  }

  override def equals(other: Any) = {
    other match {
      case that: Property[T] => (that canEqual this) && value == that.value
      case _ => false
    }
  }

  override def hashCode() = {
    val prime = 41
    prime + value.hashCode
  }

}


