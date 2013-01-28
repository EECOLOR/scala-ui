package ee.ui.properties

import ee.ui.observable.Observable
import ee.ui.observable.ObservableValue
import ee.ui.events.Event

trait ObservableProperty[T] extends ObservableValue[T] {

  val change = new Event[(T, T)]
  
  protected def valueChange(oldValue: T, newValue: T) = {
    notify(newValue)
    change.fire(oldValue -> newValue)
  }
}