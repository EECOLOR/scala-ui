package ee.ui.members.detail

import org.specs2.mutable.Specification
import ee.ui.members.Property
import ee.ui.members.Event
import utils.TypeTest
import scala.collection.mutable.ListBuffer
import utils.SubtypeTest

class TupleCombinatorTest extends Specification {

  isolated
  xonly

  val prop1 = Property((1, 1l))
  val prop2 = Property("1")
  val tupleCombinator = new TupleCombinator(prop1)
  val combined: Property[(Int, Long, String)] = tupleCombinator | prop2

  def changeValue() = combined.value = (2, 2l, "2")
  
  "TupleCombinator" should {

    "extend the correct type" in {
      SubtypeTest[TupleCombinator[_] <:< ReadOnlyTupleCombinator[_]]
    }
    
    "create a CombinedPropertyBase with Property" in {

      TypeTest[Property[(Int, Long, String)]].forInstance(combined)

      combined must beAnInstanceOf[CombinedPropertyBase[_, _, _, _, _]]
    }
    
    "set the correct values" in {
      changeValue()
      prop1.value === (2, 2l)
      prop2.value === "2"
    }
    
    "fire the correct change events" in {
      val changes = ListBuffer.empty[(Int, Long, String)]
      combined.change { changes += _ }
      changeValue()
      changes.toSeq === Seq((2, 2l, "2"))
    }
    
    "fire the correct value change events" in {
      val changes = ListBuffer.empty[((Int, Long, String), (Int, Long, String))]
      combined.valueChange { changes += _ }
      changeValue()
      changes.toSeq === Seq((1, 1l, "1") -> (2, 2l, "2"))
      
    }
  }
}