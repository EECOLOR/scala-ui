package ee.ui.members.detail

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.members.Property
import scala.collection.mutable.ListBuffer

class MappedPropertyTest extends Specification {

  xonly
  isolated

  val map: String => Int = { _.toInt }
  val reverse: Int => String = { _.toString }
  val prop = Property("1")
  val mapped = new MappedProperty(map, reverse, prop)
  def changeValue() = mapped.value = 2

  "MappedProperty" should {

    "extend MappedReadOnlyProperty with Property" in {
      SubtypeTest[MappedProperty[String, Int] <:< MappedReadOnlyProperty[String, Int] with Property[Int]]
    }

    "set the correct values" in {
      changeValue()
      prop.value === "2"
    }

    "dispatch the correct change events" in {
      val events = ListBuffer.empty[Int]
      mapped.change { events += _ }
      changeValue()
      events.toSeq === Seq(2)
    }

    "dispatch the correct value change events" in {
      val events = ListBuffer.empty[(Int, Int)]
      mapped.valueChange { events += _ }
      changeValue()
      events.toSeq === Seq((1, 2))
    }
  }
}