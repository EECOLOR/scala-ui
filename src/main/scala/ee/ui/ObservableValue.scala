package ee.ui

trait ObservableValue[T] {
	def onValueChange(listener: T => Unit): Unit
	def onValueChangeIn(listener: PartialFunction[T, Unit]): Unit
}