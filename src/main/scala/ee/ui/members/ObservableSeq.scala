package ee.ui.members

import ee.ui.events.Change
import ee.ui.system.AccessRestriction

trait ObservableSeq[T] extends Seq[T] {
  val change = ReadOnlyEvent[Change[T]]

  protected def +=(element: T): this.type
  protected def -=(element: T): this.type
  protected def clear(): Unit
}

object ObservableSeq {
  def apply[T](elements: T*): ObservableSeq[T] = ObservableArrayBuffer(elements: _*)
  def empty[T]: ObservableSeq[T] = ObservableArrayBuffer.empty[T]

  def add[T](o: ObservableSeq[T], element: T)(implicit ev: AccessRestriction): Unit =
    o += element

  def remove[T](o: ObservableSeq[T], element: T)(implicit ev: AccessRestriction): Unit =
    o -= element

  def clear[T](o: ObservableSeq[T])(implicit ev: AccessRestriction): Unit =
    o.clear()

}