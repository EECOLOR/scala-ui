package ee.uiFramework
import ee.scala.plugin.implicitMetadata.Metadata
import scala.collection.mutable.ListBuffer

trait State {
    private val _values = ListBuffer[Value]()
    
	def ::(metadata:Metadata):Value = {
      val value = new Value(metadata)
      _values += value
      value
    }
	
	class Value(metadata:Metadata) {
        
        private var _value:Option[Any] = None
        
        def value = _value
        
        def > (value:Any) = _value = Some(value)
    }
}