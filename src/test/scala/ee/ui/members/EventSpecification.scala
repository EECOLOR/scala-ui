package ee.ui.members

import org.specs2.Specification
import scala.collection.mutable.ListBuffer
import ee.ui.TestUtils
import scala.tools.reflect.ToolBoxError
import scala.collection.mutable.ListBuffer
import ee.ui.members.details.CanTypeObservable
import ee.ui.members.details.Observable

object EventSpecification extends Specification {

  def is = "Event specification".title ^
    hide ^ end
  //show ^ end

  def hide = args(xonly = true) ^ show ^ end

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
      { // canceling subscription

        val event = Event[Int]

        var eventFired = 0

        val subscription = event {
          eventFired += 1
        }

        event fire 1

        subscription.unsubscribe()

        event fire 1

        eventFired ==== 1
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

        val typeCheck = event: ReadOnlyEvent[String]

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

        val typeCheck = event: ReadOnlyEvent[Int]

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

        val typeCheck = event: ReadOnlyEvent[String]

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
      	public event as ReadOnlyEvent
      """ ^
      { // ReadOnlyEvent

        def result = TestUtils.eval("""
          |import ee.ui.members.Event
          |import ee.ui.members.ReadOnlyEvent
          |  
          |val myObj = new {
          |  private val writableEvent = Event[Int]
          |  val event:ReadOnlyEvent[Int] = writableEvent
          |}
          |
          |// will not compile:
          |myObj.event.fire
        """.stripMargin)

        result must throwA[ToolBoxError].like {
          case e =>
            e.getMessage must contain("value fire is not a member of ee.ui.members.ReadOnlyEvent[Int]")
        }
      } ^
      p ^ "Combining events" ^
      { // Combined event |
        val event1 = Event[Int]
        val event2 = Event[Int]

        val combined = event1 | event2

        val events = ListBuffer.empty[Int]

        combined { events += _ }

        event1 fire 1
        event2 fire 2

        val typeCheck = combined: ReadOnlyEvent[Int]

        (events must have size (2)) and
          (Seq(1, 2) ==== events.toSeq)

      } ^
      "Check types of combined events" ! {
        val event1 = Event[Int] | Event[Long]
        val event2 = Event[Int] | Event[Int]
        val event3 = Event[Long] | Event[Int]

        val typeCheck1 = event1: ReadOnlyEvent[AnyVal]
        val typeCheck2 = event2: ReadOnlyEvent[Int]
        val typeCheck3 = event3: ReadOnlyEvent[AnyVal]
        ok
      }
  p ^ "One time observers" ^
    { // One time observer

      var eventFireCount = 0

      val event = Event[Int]
      event.once apply {
        eventFireCount += 1
      }

      event fire 0
      event fire 0

      eventFireCount === 1
    } ^
    end
}