package ee.ui.members.details

trait OptionalBindingSource[T] {
  def when(condition: => Boolean):ObservableValue[T]
}