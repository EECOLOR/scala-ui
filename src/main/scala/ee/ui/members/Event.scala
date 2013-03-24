package ee.ui.members

import scala.collection.mutable.ListBuffer

case class Event[T]() {
  val observers = ListBuffer[T => Unit]()
  
  def fire(information:T) = 
    observers.foreach(_(information))
    
  def observe(observer:T => Unit) = 
    observers += observer
    
  def apply(observer:T => Unit) = observe(observer)
}