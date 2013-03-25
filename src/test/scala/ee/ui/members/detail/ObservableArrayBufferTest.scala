package ee.ui.members.detail

import org.specs2.mutable.Specification
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import ee.ui.events.Change
import ee.ui.events.Add
import ee.ui.events.Remove
import ee.ui.events.Clear
import ee.ui.members.ObservableSeq
import ee.ui.members.ObservableArrayBuffer

class ObservableArrayBufferTest extends Specification {
  xonly
  isolated
  
  val observableArrayBuffer = new ObservableArrayBuffer[Int]
  val resultingEvents = ListBuffer.empty[Change[Int]]
  val element1 = 1
  val element2 = 1
  val elements = Seq(element1, element2)
  
  "ObservableArrayBuffer" should {
    "be an instance of ObservableSeq" in {
      observableArrayBuffer must beAnInstanceOf[ObservableSeq[Int]]
    }
    "be an instance of ArrayBuffer" in {
      observableArrayBuffer must beAnInstanceOf[ArrayBuffer[Int]]
    }
    "dispatch an event when" in {
      "one element is added" in {
        
        observableArrayBuffer.change { resultingEvents += _ }
        observableArrayBuffer += element1

        resultingEvents.toSeq === Seq(Add(0, element1))
      }
      "multiple nodes are added" in {
        
        observableArrayBuffer.change { resultingEvents += _ }
        observableArrayBuffer ++= elements

        resultingEvents.toSeq === Seq(Add(0, elements(0)), Add(1, elements(1)))
      }
      "a node is added using a right associative method" in {

        observableArrayBuffer.change { resultingEvents += _ }
        element1 +=: observableArrayBuffer

        resultingEvents.toSeq === Seq(Add(0, element1))
      }
      "a node is updated" in {

        observableArrayBuffer += element1
        observableArrayBuffer.change { resultingEvents += _ }
        observableArrayBuffer.update(0, element2)

        resultingEvents.toSeq === Seq(Remove(0, element1), Add(0, element2))
      }
      "a node is removed by index" in {

        observableArrayBuffer += element1
        observableArrayBuffer.change { resultingEvents += _ }
        observableArrayBuffer.remove(0)

        resultingEvents.toSeq === Seq(Remove(0, element1))
      }
      "a node is removed" in {
        observableArrayBuffer += element1
        observableArrayBuffer.change { resultingEvents += _ }
        observableArrayBuffer -= element1

        resultingEvents.toSeq === Seq(Remove(0, element1))
      }
      "the group is cleared" in {
        observableArrayBuffer += element1
        observableArrayBuffer.change { resultingEvents += _ }
        observableArrayBuffer.clear

        resultingEvents.toSeq === Seq(Clear(Seq(element1)))
      }
      "inserted a collection at a given index" in {
        observableArrayBuffer ++= Seq(3, 4)
        observableArrayBuffer.change { resultingEvents += _ }
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