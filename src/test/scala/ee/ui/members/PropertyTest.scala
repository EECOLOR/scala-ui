package ee.ui.members

import org.specs2.mutable.Specification
import org.specs2.mutable.Before

class PropertyTest extends Specification {

  xonly
  isolated

  val prop1 = Property(1)
  var result = 1

  "Property" should {

    "extend ReadOnlyProperty" in {
      prop1 must beAnInstanceOf[ReadOnlyProperty[Int]]
    }

    "have a value property that can be set" in {
      prop1.value = 2
      prop1.value === 2
    }

    "have an unapply method" in {
      val Property(value) = prop1

      value === 1
    }
  }
}