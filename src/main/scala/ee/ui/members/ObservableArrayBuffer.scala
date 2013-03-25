package ee.ui.members

import scala.collection.mutable.ArrayBuffer
import ee.ui.events.Add
import ee.ui.events.Change
import ee.ui.system.RestrictedAccess
import ee.ui.events.Remove
import ee.ui.events.Clear

class ObservableArrayBuffer[T] extends ArrayBuffer[T] with ObservableSeq[T] {
  
  private def fire(c: Change[T]) = ReadOnlyEvent.fire(change, c)(RestrictedAccess)

  override def +=(element: T): this.type = {
    super.+=(element)
    fire(Add(size - 1, element))
    this
  }

  override def +=:(element: T): this.type = {
    super.+=:(element)
    fire(Add(size - 1, element))
    this
  }

  override def update(n: Int, newElement: T): Unit = {
    val oldElement = apply(n)
    super.update(n, newElement)
    fire(Remove(n, oldElement))
    fire(Add(n, newElement))
  }

  override def remove(n: Int): T = {
    val oldElement = super.remove(n)
    fire(Remove(n, oldElement))
    oldElement
  }

  override def clear(): Unit = {
    val elements = clone
    super.clear
    fire(Clear(elements))
  }

  override def insertAll(n: Int, elems: collection.Traversable[T]): Unit = {
    super.insertAll(n, elems)
    var index = n
    elems.view.foreach { element =>
      fire(Add(index, element))
      index += 1
    }
  }
}

object ObservableArrayBuffer {
  def apply[T](elements:T*) = new ObservableArrayBuffer[T] ++= elements
  def empty[T] = new ObservableArrayBuffer[T]
}