package ee.ui.members

import org.specs2.mutable.Specification
import org.specs2.mutable.Before

class PropertyTest extends Specification {
 
  xonly
  isolated

  val prop1 = Property(1)
  
  "Property" should {
    
    "have a default value that determines the type" in {
      prop1.defaultValue === 1
    }
    
    "have a value property" in {
      
      "that has the default value by default" in {
        prop1.value === 1
      }
      "that can be set" in {
        prop1.value = 2
        prop1.value === 2
      }
    }
    
    "automatically convert to its value if appropriate" in {
      val propValue:Int = prop1
      propValue === 1
    }
    
    "have a readonly change event" in {
      // how to check if it's not an event, but a read only event? probably using the compiler check
      todo
    }
    
    "be able to dispatch changes" in {
      var result = 1
      prop1.change { information =>
        result = information
      }
      prop1.value = 2
      
      result === 2
    }
    
    "only dispatch changes if the value changes" in {
      var result = 1
      prop1.change { information =>
        result = 2
      }
      prop1.value = 1
      
      result === 1
    }
    
    "have an unapply method" in {
      val Property(value) = prop1
      
      value === 1
    }
  }
}