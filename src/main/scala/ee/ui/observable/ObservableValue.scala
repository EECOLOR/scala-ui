package ee.ui.observable

trait ObservableValue[T] {
	def onValueChange(listener: T => Unit): Unit
	def onValueChangeIn(listener: PartialFunction[T, Unit]): Unit
}