package ee.ui.members

import org.specs2.mutable.Specification

class EventTest extends Specification {

  xonly
  isolated
  
  val event = Event[Int]()
  var result = 0
  
  "Event" should {
    
    "have the ability to fire" in {
      event.observe {  
        result = _
      }
      event fire 1
      
      result === 1
    }
    
    "extend readonly event" in {
      event must beAnInstanceOf[ReadOnlyEvent[Int]]
    }
    
  } 
  
}