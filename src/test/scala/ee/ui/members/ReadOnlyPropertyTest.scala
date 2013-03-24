package ee.ui.members

import org.specs2.mutable.Specification
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.system.RestrictedAccess
import utils.TypeTest

class ReadOnlyPropertyTest extends Specification {

  xonly
  isolated

  val prop1 = ReadOnlyProperty(1)
  var result = 1
  def setValue(value: Int) = ReadOnlyProperty.setValue(prop1, value)(RestrictedAccess)

  "ReadOnlyPropert" should {
    "have a default value" in {
      prop1.defaultValue === 1
    }
    "have a value that is a default value" in {
      prop1.value === 1
    }

    "not be able to set a value directly" in {
      def result = TestUtils.eval("""
          |import ee.ui.members.ReadOnlyProperty
          |  
          |val myObj = new {
          |  val prop = ReadOnlyProperty(1)
          |}
          |
          |// will not compile:
          |myObj.prop.value = 2
        """.stripMargin)

      result must throwA[ToolBoxError].like {
        case e =>
          e.getMessage must contain("method value_= in trait ReadOnlyProperty cannot be accessed in ee.ui.members.ReadOnlyProperty[Int]")
      }
    }

    "be able to set a value using a detour" in {
      setValue(2)
      prop1.value === 2
    }

    "be able to dispatch changes" in {
      prop1.change { information =>
        result = information
      }
      setValue(2)

      result === 2
    }

    "only dispatch changes if the value changes" in {
      prop1.change { information =>
        result = 2
      }
      setValue(1)

      result === 1
    }

    "have a read only change event" in {
      TypeTest[ReadOnlyEvent[Int]].forInstance(prop1.change)
    }
    
    "automatically convert to its value if appropriate" in {
      val propValue: Int = prop1
      propValue === 1
    }

    "have an unapply method" in {
      val ReadOnlyProperty(value) = prop1

      value === 1
    }

  }

}