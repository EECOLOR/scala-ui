package ee.ui.properties

trait Variable[T] extends Value[T] {
  def default: T

  private var _value: T = default

  override def value: T = _value
  def value_=(value: T): Unit =
    if (value != this.value) {
      setValue(value)
    }
  
  protected def setValue(value: T): Unit = _value = value

  def reset = value = default
  def isDefault = value == default
  def isChanged = value != default

}
