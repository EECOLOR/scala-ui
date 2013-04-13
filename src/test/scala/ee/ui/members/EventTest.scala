package ee.ui.members

import org.specs2.mutable.Specification
import utils.SignatureTest
import utils.SubtypeTest
import ee.ui.members.detail.Observers
import ee.ui.members.detail.CombinedEventBase

object EventTest extends Specification {

  xonly

  "Event" should {

    "extend the correct types" in {
      SubtypeTest[Event[Int] <:< ReadOnlyEvent[Int] with Observers[Int => Unit]]
    }

    "have a public fire method" in {
      SignatureTest[Event[Int], Int, Unit](_.fire(_))
    }
    
    "call notify on fire" in {
      var notifyCalled = false
      val event = new Event[Int] {
        override def notify(invoke:(Int => Unit) => Unit) = notifyCalled = true 
      }
      event fire 1
      
      notifyCalled
    }
    
    "have a | method" in {
      SignatureTest[Event[Int], Event[Long], Event[AnyVal]](_ | _)
    }
    
    "create a CombinedEventBase instance with Event in case of a | call" in {
      val combined = Event[Int] | Event[Long]
      combined must beAnInstanceOf[CombinedEventBase[_, _, _] with Event[_]]
    }
  }
}