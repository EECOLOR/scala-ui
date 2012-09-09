package ee.ui

import scala.collection.mutable.Subscriber
import scala.collection.script.Message
import scala.collection.mutable.ObservableBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Undoable

class Group extends Component {
	val children:ObservableBuffer[Component] = new ArrayBuffer[Component] with ObservableBuffer[Component]
}

