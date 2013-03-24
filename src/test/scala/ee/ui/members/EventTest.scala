package ee.ui.members

import org.specs2.mutable.Specification

class EventTest extends Specification {

  xonly
  isolated
  
  val event = Event[Int]()
  def fireOne = event fire 1
  
  "Event" should {
    
    "have the ability to observe and fire" in {
      var result = 0
      event.observe {  
        result = _
      }
      fireOne
      
      result === 1
    }
    
    "have a simpler method of observe" in {
      var result = 0
      event { 
        result = _
      }
      fireOne
      
      result === 1
    }
  } 
  
}