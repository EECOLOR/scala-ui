package ee.ui.members.detail

import scala.annotation.implicitNotFound
import scala.collection.mutable.ListBuffer

import org.specs2.mutable.Specification

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SubtypeTest

class MappedReadOnlyPropertyTest extends Specification {

  xonly
  isolated

  val map: String => Int = { _.toInt }
  val prop = Property("1")
  val mapped = new MappedReadOnlyProperty(map, prop)
  def changeValue() = prop.value = "2"

  "MappedReadOnlyProperty" should {
    
    "extend ReadOnlyProperty" in {
      SubtypeTest[MappedReadOnlyProperty[String, Int] <:< ReadOnlyProperty[Int]]
    }
    
    "have the correct default value" in {
      mapped.defaultValue === 1
    }
    
    "have the correct value" in {
      changeValue()
      mapped.value === 2
    }
    
    "send the correct change events" in {
      val events = ListBuffer.empty[Int]
      mapped.change { events += _ }
      changeValue()
      events.toSeq === Seq(2)
    }
    
    "send the correct value change events" in {
      val events = ListBuffer.empty[(Int, Int)]
      mapped.valueChange { events += _ }
      changeValue()
      events.toSeq === Seq((1, 2))
    }
    
    "throw an exception if value is set" in {
      new MappedReadOnlyProperty(map, prop) {
        value = 2
      } must throwA[UnsupportedOperationException]
    }
  }

}