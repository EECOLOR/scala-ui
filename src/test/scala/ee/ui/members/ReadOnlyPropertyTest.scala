package ee.ui.members

import org.specs2.mutable.Specification
import utils.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.system.RestrictedAccess

class ReadOnlyPropertyTest extends Specification {

  xonly
  isolated

  val prop1 = ReadOnlyProperty(1)
  
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
      ReadOnlyProperty.setValue(prop1, 2)(RestrictedAccess)
      prop1.value === 2
    }
  }

}