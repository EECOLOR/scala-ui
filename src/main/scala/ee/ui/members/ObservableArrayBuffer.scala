package ee.ui.members

import scala.collection.mutable.ArrayBuffer
import ee.ui.members.details.ObservableBuffer

class ObservableArrayBuffer[T](initialSize:Int) extends ArrayBuffer[T](initialSize) with ObservableBuffer[T] {
  def this() = this(16)
}

object ObservableArrayBuffer {
  def apply[T]() = new ObservableArrayBuffer[T]()
}