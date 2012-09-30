package ee.ui.properties

import scala.collection.mutable.ArrayBuffer

class ObservableArrayBuffer[T](initialSize:Int) extends ArrayBuffer[T](initialSize) with ObservableBuffer[T] {
  def this() = this(16)
}