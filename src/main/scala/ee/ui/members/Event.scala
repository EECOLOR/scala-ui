package ee.ui.members

import scala.collection.mutable.ListBuffer

case class Event[T]() extends ReadOnlyEvent[T] {
  def fire(information:T) = 
    observers.foreach(_(information))
}