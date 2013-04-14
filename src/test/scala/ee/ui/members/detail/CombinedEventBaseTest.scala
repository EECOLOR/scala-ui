package ee.ui.members.detail

import scala.collection.mutable.ListBuffer

import org.specs2.mutable.Specification

import ee.ui.members.Event
import ee.ui.members.ReadOnlyEvent
import utils.SubtypeTest

class CombinedEventBaseTest extends Specification {

  xonly
  isolated

  val event1 = Event[Long]
  val event2 = Event[Int]
  val combined: CombinedEventBase[Long, Int, AnyVal] = new CombinedEventBase(event1, event2) {
    def fire(information: AnyVal) = ???
  }
  val results = ListBuffer.empty[AnyVal]

  def fire(value1:Long = 1l, value2:Int = 2) = {
    event1 fire value1
    event2 fire value2
  }
  
  "CombinedEventBase" should {
    
    "extend ReadOnlyEvent" in {
      SubtypeTest[CombinedEventBase[Long, Int, AnyVal] <:< ReadOnlyEvent[AnyVal]]
    }
    
    "represent a combination of two events" in {
      combined { results += _ }
      fire()

      results.toSeq === Seq(1, 2)
    }
    
    "should be able to enable, disable and unsubscribe" in {
      val subscription = combined { results += _ }
      subscription.disable()
      fire()
      subscription.enable()
      fire(3, 4)
      subscription.unsubscribe()
      fire(5, 6)

      results.toSeq === Seq(3, 4)
    }
  }
}