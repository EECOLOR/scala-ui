package ee.ui.members.details

trait Binding {
  def unbind(): Unit
}

case class SimpleBinding[S <% T, T](
  source: ObservableValue[S],
  target: Variable[T],
  default: S) extends Binding {

  target.value = default

  val subscription = source.change { value =>
    target.value = value
  }

  def unbind() = subscription.unsubscribe()
}

case class ConditionalBinding[S <% T, T](
  source: ObservableValue[S],
  target: Variable[T],
  default: S,
  condition: S => Boolean) extends Binding {

  if (condition(default)) target.value = default
  
  val subscription = source.change filter condition apply { value =>
    target.value = value
  }
  
  def unbind() = subscription.unsubscribe()
}
