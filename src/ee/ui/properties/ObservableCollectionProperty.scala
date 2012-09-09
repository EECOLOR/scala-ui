package ee.ui.properties

import scala.collection.mutable.Publisher
import scala.collection.script.Message
import scala.collection.mutable.Undoable
import scala.collection.mutable.Undoable
import scala.collection.mutable.BufferProxy
import scala.collection.mutable.Buffer

class ObservableCollectionProperty[V, T <: Buffer[V] with Publisher[Message[V] with Undoable]](default:T) extends BufferProxy[V] {
    def self:Buffer[V] = default
    
    type Handler = (Message[V] with Undoable, this.type) => Boolean
    type Listener = (Message[V] with Undoable, this.type) => Unit

    private var handlers: Seq[Handler] = Seq.empty

    def value = default
    
    default subscribe new default.Sub {
        def notify(publisher:default.Pub, event:Message[V] with Undoable) = {
             handlers.forall { handler =>
               handler(event, ObservableCollectionProperty.this)
             }
        }
    }
    
    def handleChange(handler: Handler): Unit = handler +: handlers
    def handleChangeMatch(handler: PartialFunction[(Message[V] with Undoable, ObservableCollectionProperty[V, T]), Boolean]): Unit =
        handleChange { (e, p) =>
            val arguments = (e, p)
            if (handler isDefinedAt arguments) handler(arguments)
            else true
        }

    def onChange(handler: Listener): Unit =
        handleChange { (e, p) =>
            handler(e, p)
            true
        }
    def onChangeMatch(handler: PartialFunction[(Message[V] with Undoable, ObservableCollectionProperty[V, T]), Unit]): Unit =
    	onChange { (e, p) =>
    	    val arguments = (e, p)
    		if (handler isDefinedAt arguments) handler(arguments)
    	}
}