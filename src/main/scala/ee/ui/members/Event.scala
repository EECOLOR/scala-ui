package ee.ui.members

import scala.collection.mutable.ListBuffer
import ee.ui.members.detail.Subscription
import ee.ui.members.detail.Observers

case class Event[T]() extends ReadOnlyEvent[T] with Observers[T => Unit] {
  
  def fire(information: T) =
    notify(_(information))
}