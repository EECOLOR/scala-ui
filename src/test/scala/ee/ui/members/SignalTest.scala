package ee.ui.members

import org.specs2.mutable.Specification
import utils.SignatureTest
import ee.ui.members.detail.Subscription

class SignalTest extends Specification {
  
  xonly
  isolated
  
  val signal = Signal()
  var fired = false
  
  "Signal" should {
    "be created easily" in {
      Signal()
      ok
    }
    "have the ability to fire" in {
      signal.observe {  
        fired = true
      }
      signal.fire()
      
      fired
    }
    
    "should extend ReadOnlySignal" in {
      Signal() must beAnInstanceOf[ReadOnlySignal]
    }
  }
}