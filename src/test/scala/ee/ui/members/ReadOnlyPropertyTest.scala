package ee.ui.members

import org.specs2.mutable.Specification
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.system.RestrictedAccess
import utils.TypeTest
import utils.SignatureTest
import ee.ui.members.detail.MappedReadOnlyProperty
import ee.ui.members.detail.BindingSource
import ee.ui.members.detail.ReadOnlyTupleCombinator

class ReadOnlyPropertyTest extends Specification {

  xonly
  isolated

  val prop1 = ReadOnlyProperty(1)
  var result = 1
  def setValue(value: Int) = ReadOnlyProperty.setValue(prop1, value)(RestrictedAccess)

  class StubReadOnlyProperty extends ReadOnlyProperty[Int] {
    val defaultValue = 1
    def value = 1
    val change = ReadOnlyEvent[Int]
    val valueChange = ReadOnlyEvent[(Int, Int)]
    override protected def value_=(value: Int): Unit = {}
  }

  "ReadOnlyPropert" should {

    "have the correct signatures" in {
      SignatureTest[ReadOnlyProperty[Int], Int](_.defaultValue)
      SignatureTest[ReadOnlyProperty[Int], Int](_.value)
      SignatureTest[ReadOnlyProperty[Int], ReadOnlyEvent[Int]](_.change)
      SignatureTest[ReadOnlyProperty[Int], ReadOnlyEvent[(Int, Int)]](_.valueChange)
    }

    "have the correct protected methods" in {
      new StubReadOnlyProperty {
        override protected def value_=(value: Int): Unit = {}
        override protected def fireChange(value: Int): Unit = {}
        override protected def fireValueChange(values: (Int, Int)): Unit = {}
      }
      ok
    }

    "should be able to map" in {
      val mapped: ReadOnlyProperty[String] = prop1 map (_.toString)
      mapped must beAnInstanceOf[MappedReadOnlyProperty[_, _]]
    }

    "have an apply method" in {
      SignatureTest[ReadOnlyProperty.type, Int, ReadOnlyProperty[Int]](_ apply _)
      ReadOnlyProperty[Int](1) must beAnInstanceOf[Property[Int]]
    }

    "be able to set a value using a detour" in {
      ReadOnlyProperty.setValue(prop1, 2)(RestrictedAccess)
      prop1.value === 2
    }

    "automatically convert to its value if appropriate" in {
      val propValue: Int = prop1
      propValue === 1
    }

    "have an unapply method" in {
      val ReadOnlyProperty(value) = prop1
      value === 1
    }
    
    "be able to be created from a ReadOnlyEvent" in {
      val prop:ReadOnlyProperty[Option[String]] = ReadOnlyEvent[String]
      ok
    }
    
    "be able to convert to a bindingsource" in {
      val b:BindingSource[Int] = prop1
      ok
    }
    
    "have an implicit conversion to TupleCombinator" >> {

      "for properties that contain tuples" in {
        val combinator: ReadOnlyTupleCombinator[(String, Int)] = ReadOnlyProperty("1" -> 1)
        ok
      }

      "for properties that contain options" in {
        val combinator: ReadOnlyTupleCombinator[Tuple1[Option[String]]] = ReadOnlyProperty[Option[String]](None)
        ok
      }

      "for other properties" in {
        val combinator: ReadOnlyTupleCombinator[Tuple1[String]] = ReadOnlyProperty("")
        ok
      }
    }
  }
}