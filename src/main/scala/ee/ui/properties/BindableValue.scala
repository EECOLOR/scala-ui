package ee.ui.properties

import ee.ui.observable.ObservableValue
import ee.ui.observable.Subscription

trait BindableValue[T] {
	def value_=(value: T):Unit
	
	def bindTo(observableValue:ObservableValue[T]):Subscription = {
	  value_=(observableValue.value)
	  observableValue foreach value_= _
	}
	
	def <== = bindTo _
}