package ee.ui.members

import org.specs2.mutable.Specification
import org.specs2.mutable.Before

class PropertyTest extends Specification {

  //xonly
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
      val Property(value) = prop1

      value === 1
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
  }
}