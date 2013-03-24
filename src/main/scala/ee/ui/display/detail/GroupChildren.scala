package ee.ui.display.detail

import ee.ui.display.Node
import scala.collection.mutable.ArrayBuffer
import ee.ui.members.ReadOnlyEvent
import ee.ui.events.Change
import scala.collection.mutable.Buffer
import ee.ui.system.RestrictedAccess
import ee.ui.events.Add
import ee.ui.events.Remove
import ee.ui.events.Clear

class GroupChildren extends ArrayBuffer[Node] with Buffer[Node] {
  def apply(nodes: Node*) =
    this ++= nodes

  val change = ReadOnlyEvent[Change[Node]]()

  private def fire(c: Change[Node]) = ReadOnlyEvent.fire(change, c)(RestrictedAccess)

  override def +=(element: Node): this.type = {
    super.+=(element)
    fire(Add(size - 1, element))
    this
  }

  override def +=:(element: Node): this.type = {
    super.+=:(element)
    fire(Add(size - 1, element))
    this
  }

  override def update(n: Int, newElement: Node): Unit = {
    val oldElement = apply(n)
    super.update(n, newElement)
    fire(Remove(n, oldElement))
    fire(Add(n, newElement))
  }

  override def remove(n: Int): Node = {
    val oldElement = super.remove(n)
    fire(Remove(n, oldElement))
    oldElement
  }

  override def clear(): Unit = {
    val elements = clone
    super.clear
    fire(Clear(elements))
  }

  override def insertAll(n: Int, elems: collection.Traversable[Node]): Unit = {
    super.insertAll(n, elems)
    var index = n
    elems.view.foreach { element =>
      fire(Add(index, element))
      index += 1
    }
  }
}