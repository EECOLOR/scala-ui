package ee.ui.members

import scala.collection.mutable.ListBuffer
import org.specs2.mutable.Specification
import ee.ui.events.Add
import ee.ui.events.Change
import ee.ui.events.Clear
import ee.ui.events.Remove
import utils.SubtypeTest
import scala.collection.mutable.ArrayBuffer

class ObservableArrayBufferTest extends Specification {
  
  xonly
  isolated
  
  val observableArrayBuffer = new ObservableArrayBuffer[Int]
  val resultingEvents = ListBuffer.empty[Change[Int]]
  val element1 = 1
  val element2 = 2
  val elements = Seq(element1, element2)
  observableArrayBuffer.change { resultingEvents += _ }
  def silently(code: => Unit) = {
    code
    resultingEvents.clear()
  }
  def silentlyAdd1() = silently { observableArrayBuffer += element1 }
    
  
  "ObservableArrayBuffer" should {
    
    "extend the correct types" in {
      SubtypeTest[ObservableArrayBuffer[Int] <:< ArrayBuffer[Int] with ObservableSeq[Int]]
    }
    
    "dispatch an event when" >> {
        
      "one element is added" in {
        
        observableArrayBuffer += element1

        resultingEvents.toSeq === Seq(Add(0, element1))
      }
      
      "multiple elements are added" in {
        
        observableArrayBuffer ++= elements

        resultingEvents.toSeq === Seq(Add(0, elements(0)), Add(1, elements(1)))
      }
      
      "an element is added using a right associative method" in {

        element1 +=: observableArrayBuffer

        resultingEvents.toSeq === Seq(Add(0, element1))
      }
      
      "an element is updated" in {

        silentlyAdd1()
        observableArrayBuffer.update(0, element2)

        resultingEvents.toSeq === Seq(Remove(0, element1), Add(0, element2))
      }
      
      "an element is removed by index" in {

        silentlyAdd1()
        observableArrayBuffer.remove(0)

        resultingEvents.toSeq === Seq(Remove(0, element1))
      }
      
      "an element is removed" in {
        
        silentlyAdd1()
        observableArrayBuffer -= element1

        resultingEvents.toSeq === Seq(Remove(0, element1))
      }
      
      "the buffer is cleared" in {
        
        silentlyAdd1()
        observableArrayBuffer.clear

        resultingEvents.toSeq === Seq(Clear(Seq(element1)))
      }
      
      "inserted a collection at a given index" in {
        silently { observableArrayBuffer ++= Seq(3, 4) }
        observableArrayBuffer.insertAll(1, Seq(element1, element2))
        
        resultingEvents.toSeq === Seq(Add(1, element1), Add(2, element2))
      }
    }
    "have an easy contructor" in {
      ObservableArrayBuffer("test")
      ok
    }
    "have an empty constructor" in {
      ObservableArrayBuffer.empty[String]
      ok
    }
  }
}