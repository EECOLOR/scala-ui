package ee.ui.properties

import scala.collection.mutable.Publisher
import scala.collection.script.Message
import scala.collection.mutable.Undoable
import scala.collection.mutable.Undoable
import scala.collection.mutable.BufferProxy
import scala.collection.mutable.Buffer
import ee.ui.Observable

class ObservableCollectionProperty[V, T <: Buffer[V] with Publisher[Message[V] with Undoable]](default:T) extends BufferProxy[V] with Observable[Message[V] with Undoable] {
    def self:Buffer[V] = default
    
    def value = default
    
    default subscribe new default.Sub {
        def notify(publisher:default.Pub, event:Message[V] with Undoable) = 
          ObservableCollectionProperty.this notify event
    }
}