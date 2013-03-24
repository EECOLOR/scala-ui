package ee.ui.members

import org.specs2.mutable.Specification

class EventTest extends Specification {

  xonly
  isolated
  
  val event = Event[Int]()
  def fireOne = event fire 1
  var result = 0
  
  "Event" should {
    
    "have the ability to observe and fire" in {
      event.observe {  
        result = _
      }
      fireOne
      
      result === 1
    }
    
    "have a simpler method of observe" in {
      event { 
        result = _
      }
      fireOne
      
      result === 1
    }
    
    "extend readonly event" in {
      event must beAnInstanceOf[ReadOnlyEvent[Int]]
    }
  } 
  
}