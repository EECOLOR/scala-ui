package ee.ui.members

import org.specs2.mutable.Specification
import org.specs2.mutable.Before
import scala.collection.mutable.ListBuffer
import utils.SubtypeTest
import utils.SignatureTest
import ee.ui.members.detail.BindingSource
import ee.ui.members.detail.TupleCombinator

class PropertyTest extends Specification {

  xonly
  isolated

  class StubProperty extends Property[Int] {
    val defaultValue = 1
    def value = 1
    def setValue(value: Int): Unit = {}
    val change = ReadOnlyEvent[Int]
    val valueChange = ReadOnlyEvent[(Int, Int)]
  }

  "Property" should {

    "extend ReadOnlyProperty" in {
      SubtypeTest[Property[Int] <:< ReadOnlyProperty[Int]]
    }

    "have a value property" >> {

      "with signature" in {
        SignatureTest[Property[Int], Int, Unit](_.value = _)
      }

      "that can be set" in {
        val prop = Property(1)
        prop.value = 2
        prop.value === 2
      }

      "that does not call setValue and fireEvents" in {
        var methodCalled = false

        val prop = new StubProperty {
          override def setValue(value: Int): Unit = methodCalled = true
          override def fireEvents(oldValue: Int, newValue: Int) = methodCalled = true
        }

        prop.value = 1

        !methodCalled
      }
    }

    "have a fireEvents method that calls fireChange and fireValueChange" in {
      var fireChangeCalled = false
      var fireValueChangeCalled = false

      val prop = new StubProperty {

        override def fireChange(value: Int) =
          fireChangeCalled = value == 2

        override def fireValueChange(values: (Int, Int)) =
          fireValueChangeCalled = values._1 == 1 && values._2 == 2

        fireEvents(1, 2)
      }

      fireValueChangeCalled and fireValueChangeCalled
    }

    "have a bind method" in {
      SignatureTest[Property[Int], BindingSource[Int], Unit](_ <== _)
    }
    
    "have a bind method that allows binding to a property of a subtype" in {
      val anyValProp = Property[AnyVal](0)
      val intProp = Property(1)
      anyValProp <== intProp
      anyValProp.value === 1
    }

    "have a birectional bind method" in {
      SignatureTest[Property[Int], Property[Int], Unit](_ <==> _)
    }

    "have an apply method" in {
      SignatureTest[Property.type, Int, Property[Int]](_ apply _)
    }

    "have a DefaultProperty member" >> {

      "that has the correct signature" in {
        SignatureTest[Property.type, Int, Property[Int]](_.DefaultProperty(_))
      }

      "that behaves correctly" in {
        val prop = Property.DefaultProperty(1)
        prop.value === 1
        prop.value = 2
        prop.value === 2
      }

      "has a change and valueChange event" in {
        SignatureTest[Property.DefaultProperty[Int], ReadOnlyEvent[Int]](_.change)
        SignatureTest[Property.DefaultProperty[Int], ReadOnlyEvent[(Int, Int)]](_.valueChange)
      }
    }

    "have an unapply method" in {
      val Property(value) = new StubProperty

      value === 1
    }

    "have an implicit conversion to TupleCombinator" >> {

      "for properties that contain tuples" in {
        val combinator: TupleCombinator[(String, Int)] = Property("1" -> 1)
        ok
      }

      "for properties that contain options" in {
        val combinator: TupleCombinator[Tuple1[Option[String]]] = Property[Option[String]](None)
        ok
      }

      "for other properties" in {
        val combinator: TupleCombinator[Tuple1[String]] = Property("")
        ok
      }
    }
  }
}