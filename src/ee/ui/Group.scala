package ee.ui

import scala.collection.mutable.Subscriber
import scala.collection.script.Message
import scala.collection.mutable.ObservableBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Undoable
import ee.ui.nativeElements.NativeElement

class Group extends Node with NativeElement[Group] {
  
	override def nativeElement = createNativeElement
	
	val children:ObservableBuffer[Node] = new ArrayBuffer[Node] with ObservableBuffer[Node]
}

