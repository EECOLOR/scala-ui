package ee.ui.members

import scala.collection.mutable.ListBuffer
import scala.tools.reflect.ToolBoxError

import org.specs2.mutable.Specification

import ee.ui.events.Add
import ee.ui.events.Change
import ee.ui.events.Clear
import ee.ui.events.Remove
import ee.ui.system.RestrictedAccess
import utils.SignatureTest
import utils.SubtypeTest
import utils.TestUtils

object ObservableSeqTest extends Specification {
  
  xonly

  "ObservableSeq" should {
    
    "extend the correct types" in {
      SubtypeTest[ObservableSeq[Int] <:< Seq[Int]]
    }
    
    "have a change event" in {
      SignatureTest[ObservableSeq[Int], ReadOnlyEvent[Change[Int]]](_.change)
    }
    
    "have a protected add method" in {
      def result = TestUtils.eval("""
          |import ee.ui.members.ObservableSeq
          |  
          |val myObj = new {
          |  val prop = ObservableSeq(1)
          |}
          |
          |// will not compile:
          |myObj.prop += 1
          """.stripMargin)
          
          result must throwA[ToolBoxError].like {
          case e =>
          e.getMessage must contain("method += in trait ObservableSeq cannot be accessed in ee.ui.members.ObservableSeq[Int]")
      }
    }
    
    "have a protected remove method" in {
      def result = TestUtils.eval("""
          |import ee.ui.members.ObservableSeq
          |  
          |val myObj = new {
          |  val prop = ObservableSeq(1)
          |}
          |
          |// will not compile:
          |myObj.prop -= 1
          """.stripMargin)
          
          result must throwA[ToolBoxError].like {
          case e =>
          e.getMessage must contain("method -= in trait ObservableSeq cannot be accessed in ee.ui.members.ObservableSeq[Int]")
      }
    }
    
    "have an apply method" in {
      SignatureTest[ObservableSeq.type, Seq[String], ObservableSeq[String]](_.apply( _: _*))
      ObservableSeq("") must beAnInstanceOf[ObservableArrayBuffer[_]]
    }
    
    "have an empty method" in {
      SignatureTest[ObservableSeq.type, ObservableSeq[String]](_.empty[String])
    }
    
    "be able to add and remove and clear elements using a detour" in {
      val changeEvents = ListBuffer.empty[Change[Int]]
      val observableSeq = ObservableSeq.empty[Int]
      observableSeq.change { changeEvents += _ }
      
      ObservableSeq.add(observableSeq, 1)(RestrictedAccess)
      ObservableSeq.remove(observableSeq, 1)(RestrictedAccess)
      ObservableSeq.clear(observableSeq)(RestrictedAccess)

      changeEvents.toSeq === Seq(Add(0, 1), Remove(0, 1), Clear(Seq.empty[Int]))
    }
  }
}