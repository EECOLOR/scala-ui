package ee.ui.properties

import scala.collection.mutable.ObservableBuffer
import scala.collection.mutable.ArrayBuffer

class ObservableArrayBuffer[T] extends ObservableCollectionProperty[T, ObservableBuffer[T]](
        default = new ArrayBuffer[T]() with ObservableBuffer[T] )
        
