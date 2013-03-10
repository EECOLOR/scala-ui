package ee.ui.propertiesAndEvents

import org.specs2.Specification
import ee.ui.events.Event
import scala.collection.mutable.ListBuffer
import ee.ui.events.ReadOnlyEvent
import ee.ui.TestUtils
import scala.tools.reflect.ToolBoxError

object EventSpecification extends Specification {

  def is = "Event specification".title ^
    //hide ^ end
    show ^ end

  def hide = "Specification is hidden" ^ end

  def show =
    """ Introduction
    
    	An event is a simple object that has two main properties:
    
    	1. Observers can be registered
    	2. The event can be fired
    
      """ ^
      br ^
      { // Observing and firing 
        var eventResult = 0

        val event = Event[Int]
        event { i => eventResult = i }
        event fire 1

        eventResult === 1
      } ^
      { // Ignoring the event information 
        var eventFired = false

        val event = Event[Int]
        event { eventFired = true }
        event fire 1

        eventFired === true
      } ^
      { // alternative syntax foreach
        Event[Int] foreach { e =>
          println("Event fired", e)
        }
        ok
      } ^
      { // alternative syntax observe
        Event[Int] observe { e =>
          println("Event fired", e)
        }
        ok
      } ^
      p ^
      """ Transforming events
      	
      	Events can be transformed in a few ways
      """ ^
      { // map
        var eventResult = ""

        val originalEvent = Event[Int]

        val event = originalEvent map { _.toString }
        event { eventResult = _ }

        originalEvent fire 1

        (event must beAnInstanceOf[Event[String]]) and
          (eventResult === "1")
      } ^
      { // filter
        var eventResults = ListBuffer[Int]()

        val originalEvent = Event[Int]

        val event = originalEvent filter { _ == 2 }
        event { eventResults += _ }

        originalEvent fire 1
        originalEvent fire 2
        originalEvent fire 3

        (event must beAnInstanceOf[Event[Int]]) and
          (eventResults must have size (1)) and
          (eventResults must contain(2))
      } ^
      { // collect
        var eventResults = ListBuffer[String]()

        val originalEvent = Event[Int]

        val event = originalEvent collect {
          case x if (x < 2) => "less"
        }
        event { eventResults += _ }

        originalEvent fire 1
        originalEvent fire 2
        originalEvent fire 3

        (event must beAnInstanceOf[Event[String]]) and
          (eventResults must have size (1)) and
          (eventResults must contain("less"))
      } ^
      p ^
      " Unsubscribing " ^
      { // regular event
        var eventResult = false

        val event = Event[Int]

        val subscription = event { eventResult = true }
        subscription.unsubscribe

        event fire 1

        eventResult === false
      } ^ 
      "mapped event" !
      {
        var eventResult = false

        val originalEvent = Event[Int]

        val event = originalEvent map (_.toString)
        val subscription = event { eventResult = true }
        subscription.unsubscribe

        originalEvent fire 1

        eventResult === false
      } ^ 
      "filtered event" !
      {
        var eventResult = false

        val originalEvent = Event[Int]

        val event = originalEvent filter (_ == 2)
        val subscription = event { eventResult = true }
        subscription.unsubscribe

        originalEvent fire 1
        originalEvent fire 2
        originalEvent fire 3

        eventResult === false
      } ^ 
      "collected event" !
      {
        var eventResult = false

        val originalEvent = Event[Int]

        val event = originalEvent collect {
          case x if (x < 2) => "less"
        }
        val subscription = event { eventResult = true }
        subscription.unsubscribe

        originalEvent fire 1
        originalEvent fire 2
        originalEvent fire 3

        eventResult === false
      } ^
      p ^ """ Read only events
      
      	In some cases you want to hide the fire method from the outside world.
        You can do that by declaring the actual event private and type the 
      	public event as read only event
      """ ^
      { // ReadOnlyEvent
        
        def result = TestUtils.eval("""
          import ee.ui.events.Event
          import ee.ui.events.ReadOnlyEvent
            
          val myObj = new {
            private val writableEvent = Event[Int]
            val event:ReadOnlyEvent[Int] = writableEvent
          }
        
          // will not compile:
          myObj.event.fire
        """)
        
        result must throwA[ToolBoxError].like {
          case e => 
            e.getMessage must contain("value fire is not a member of ee.ui.events.ReadOnlyEvent[Int]") 
        }
      } ^
      end
}