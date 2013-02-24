package ee.ui.properties

trait Value[T] extends Equals {
  
  def value: T

  override def canEqual(other: Any) = {
    other.isInstanceOf[Value[T]]
  }

  override def equals(other: Any) = {
    other match {
      case that: Value[T] => (that canEqual this) && value == that.value
      case _ => false
    }
  }

  override def hashCode() = {
    val prime = 41
    prime + value.hashCode
  }

}

trait LowerPriorityImplicits {
	@inline implicit def valueOfValue[T](value: Value[T]): T = value.value
}

object Value extends LowerPriorityImplicits {
}