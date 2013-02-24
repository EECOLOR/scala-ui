package ee.ui.observables

trait Subscription {
  def unsubscribe(): Unit
}

