package ee.ui

import scala.collection.mutable.Subscriber
import scala.collection.mutable.ObservableBuffer
import scala.collection.mutable.Undoable
import scala.collection.script.Message

trait Layout extends Stylable { self:Group =>
	children.subscribe(new children.Sub {
	    def notify(publisher:children.Pub, event:Message[Node] with Undoable) = {
	        
	    }
	})
}