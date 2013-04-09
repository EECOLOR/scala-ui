package ee.ui.members

import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import scala.collection.mutable.ListBuffer

class PropertyTest extends Specification {

  xonly
  isolated

  val prop1 = Property(1)
  val prop2 = Property(2)
  var result = 1

  "Property" should {

    "extend ReadOnlyProperty" in {
      prop1 must beAnInstanceOf[ReadOnlyProperty[Int]]
    }

    "have a value property that can be set" in {
      prop1.value = 2
      prop1.value === 2
    }

    "have an unapply method and a default value" in {
      prop1.value = 2
      val Property(value) = prop1

      value === 2
    }
    "not fire two change event if value does not change" in {
      var valueChanged = false
      prop1.change { valueChanged = true }
      prop1.value === 1

      !valueChanged
    }
  }
  "Bindings" >> {
    "to another property" in {
      prop1 <== prop2
      prop1.value === 2
      prop2.value = 3
      prop1.value === 3
    }
    "to another mapped property" in {
      val prop = Property("2")
      prop1 <== prop map (_.toInt)
      prop1.value === 2
    }
    "to an event" in {
      val prop = Property[Option[Int]](None)
      val event = Event[Int]
      prop <== event
      prop.value === None
      event fire 1
      prop.value === Some(1)
    }
    "to another filtered property" in {
      prop1 <== prop2 filter (_ > 2)
      prop1.value === 1
      prop2.value = 3
      prop1.value === 3
    }
    "to a method" in {
      val values = ListBuffer.empty[Int]
      prop1 bindWith { value =>
        values += value
      }
      prop1.value = 2
      values.toSeq === Seq(1, 2)
    }
    "combined to a method" in {
      val values = ListBuffer.empty[(Int, Int)]
      prop1 | prop2 bindWith { value =>
        values += value
      }
      prop1.value = 2
      prop2.value = 3
      values.toSeq === Seq((1, 2), (2, 2), (2, 3))
    }
    "be combinable" in {
      val prop1 = Property(1)
      val prop2 = Property("a")
      val prop3 = Property(0l)
      
      val newProp:Property[(Int, String, Long)] = 
        prop1 | prop2 | prop3
        
      newProp.value === (1, "a", 0l)
      prop1.value = 2
      newProp.value === (2, "a", 0l)
      prop2.value = "b"
      newProp.value === (2, "b", 0l)
      prop3.value = 1l
      newProp.value === (2, "b", 1l)
      newProp.value = (3, "c", 2l)
      prop1.value === 3
      prop2.value === "c"
      prop3.value === 2l
    }
  }
}