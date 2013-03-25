package ee.ui.members.detail

import org.specs2.mutable.Specification
import ee.ui.members.ReadOnlyEvent
import utils.TypeTest
import ee.ui.events.Change
import ee.ui.system.RestrictedAccess
import scala.collection.mutable.ListBuffer
import ee.ui.events.Add
import ee.ui.events.Remove
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.members.ObservableSeq

class ObservableSeqTest extends Specification {
  xonly
  isolated

  val testSeq = new ObservableSeq[Int] {
    def iterator: Iterator[Int] = ???
    def apply(idx: Int): Int = ???
    def length: Int = ???
    protected def +=(element: Int): this.type = ???
    protected def -=(element: Int): this.type = ???
  }

  "ObservableSeq" should {
    "be an instance of Seq" in {
      testSeq must beAnInstanceOf[Seq[Int]]
    }
    "have a change event" in {
      TypeTest[ReadOnlyEvent[Change[Int]]].forInstance(testSeq.change)
    }
    "be able to create an instance" in {
      TypeTest[ObservableSeq[String]].forInstance(ObservableSeq("test"))
    }
    "have an empty method" in {
      TypeTest[ObservableSeq[String]].forInstance(ObservableSeq.empty[String])
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
    "be able to add and remove an element using a detour" in {
      val changeEvents = ListBuffer.empty[Change[Int]]
      val observableSeq = ObservableSeq.empty[Int]
      observableSeq.change { changeEvents += _ }
      
      ObservableSeq.add(observableSeq, 1)(RestrictedAccess)
      ObservableSeq.remove(observableSeq, 1)(RestrictedAccess)

      changeEvents.toSeq === Seq(Add(0, 1), Remove(0, 1))
    }
  }
}