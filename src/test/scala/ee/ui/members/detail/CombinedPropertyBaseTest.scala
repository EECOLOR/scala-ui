package ee.ui.members.detail

import scala.collection.mutable.ListBuffer
import org.specs2.mutable.Specification
import ee.ui.members.Event
import ee.ui.members.ReadOnlyEvent
import utils.SubtypeTest
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess

class CombinedPropertyBaseTest extends Specification {

  xonly
  isolated

  def setValue[T](prop: ReadOnlyProperty[T], value: T) =
    ReadOnlyProperty.setValue(prop, value)(RestrictedAccess)

  val prop1: ReadOnlyProperty[(Int, Long)] = Property((1, 1l))
  val prop2: ReadOnlyProperty[String] = Property("1")
  val combined = new CombinedPropertyBase(prop1, prop2) {
    val change = changeEvent
    val valueChange = valueChangeEvent

    protected def value_=(value: (Int, Long, String)): Unit = ???
  }

  def setValues(value1: (Int, Long) = (1, 1l), value2: String = "2") = {
    setValue(prop1, value1)
    setValue(prop2, value2)
  }

  "CombinedPropertyBase" should {

    "extend ReadOnlyProperty" in {
      SubtypeTest[CombinedPropertyBase[(Int, Long), String, _, _, (Int, Long, String)] <:< ReadOnlyProperty[(Int, Long, String)]]
    }

    "have the correct default value" in {
      combined.value === (1, 1l, "1")
    }

    "have the correct value" in {
      setValues((2, 2l), "2")
      combined.value === (2, 2l, "2")
    }

    "fire the correct change events" in {
      val changes = ListBuffer.empty[(Int, Long, String)]
      combined.change { changes += _ }
      setValues((2, 2l), "2")
      changes.toSeq === Seq((2, 2l, "1"), (2, 2l, "2"))
    }

    "fire the correct value change events" in {
      val changes = ListBuffer.empty[((Int, Long, String), (Int, Long, String))]
      combined.valueChange { changes += _ }
      setValues((2, 2l), "2")
      changes.toSeq === Seq((1, 1l, "1") -> (2, 2l, "1"), (2, 2l, "1") -> (2, 2l, "2"))
    }

  }
}