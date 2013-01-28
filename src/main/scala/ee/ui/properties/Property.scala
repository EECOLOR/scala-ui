package ee.ui.properties

class Property[T](default: T) extends WritableProperty[T] with Equals {

  private var _value = default
  def value = _value
  def value_=(value: T) =
    if (_value != value) {
      val oldValue = _value
      _value = value
      valueChange(oldValue, value)
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


