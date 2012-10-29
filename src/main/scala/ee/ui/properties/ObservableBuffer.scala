package ee.ui.properties

import scala.collection.mutable.Buffer
import ee.ui.Observable

sealed trait Change[A]
case class Add[A](index: Int, element: A) extends Change[A]
case class Remove[A](index: Int, element: A) extends Change[A]
case class Clear[A](elements:Buffer[A]) extends Change[A]

trait ObservableBuffer[A] extends Buffer[A] with Observable[Change[A]] {

  def onChanged(listener: Change[A] => Unit): Unit = listen(listener)
  def onChanged(listener: => Unit): Unit = listen(listener)
  def onChangedIn = listenIn _

  abstract override def +=(element: A): this.type = {
    super.+=(element)
    notify(Add(size, element))
    this
  }

  abstract override def ++=(xs: TraversableOnce[A]): this.type = {
    for (x <- xs) this += x
    this
  }

  abstract override def +=:(element: A): this.type = {
    super.+=:(element)
    notify(Add(size, element))
    this
  }

  abstract override def update(n: Int, newElement: A): Unit = {
    val oldElement = apply(n)
    super.update(n, newElement)
    notify(Remove(n, oldElement))
    notify(Add(n, newElement))
  }

  abstract override def remove(n: Int): A = {
    val oldElement = apply(n)
    super.remove(n)
    notify(Remove(n, oldElement))
    oldElement
  }

  abstract override def clear(): Unit = {
    val elements = clone
    super.clear
    notify(Clear(elements))
  }

  abstract override def insertAll(n: Int, elems: collection.Traversable[A]) {
    super.insertAll(n, elems)
    var index = n
    elems.view.foreach { element =>
      notify(Add(index, element))
      index += 1
    }
  }

}
