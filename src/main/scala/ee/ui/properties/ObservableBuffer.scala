package ee.ui.properties

import scala.collection.mutable.Buffer
import ee.ui.observables.Observable
import ee.ui.events.Event
import ee.ui.events.ReadOnlyEvent

sealed trait Change[A]
case class Add[A](index: Int, element: A) extends Change[A]
case class Remove[A](index: Int, element: A) extends Change[A]
case class Clear[A](elements: Buffer[A]) extends Change[A]

trait ObservableBuffer[A] extends Buffer[A] {

  private val _change = Event[Change[A]]
  val change: ReadOnlyEvent[Change[A]] = _change

  abstract override def +=(element: A): this.type = {
    super.+=(element)
    _change fire Add(size, element)
    this
  }

  abstract override def ++=(xs: TraversableOnce[A]): this.type = {
    for (x <- xs) this += x
    this
  }

  abstract override def +=:(element: A): this.type = {
    super.+=:(element)
    _change fire Add(size, element)
    this
  }

  abstract override def update(n: Int, newElement: A): Unit = {
    val oldElement = apply(n)
    super.update(n, newElement)
    _change fire Remove(n, oldElement)
    _change fire Add(n, newElement)
  }

  abstract override def remove(n: Int): A = {
    val oldElement = apply(n)
    super.remove(n)
    _change fire Remove(n, oldElement)
    oldElement
  }

  abstract override def clear(): Unit = {
    val elements = clone
    super.clear
    _change fire Clear(elements)
  }

  abstract override def insertAll(n: Int, elems: collection.Traversable[A]) {
    super.insertAll(n, elems)
    var index = n
    elems.view.foreach { element =>
      _change fire Add(index, element)
      index += 1
    }
  }

}
