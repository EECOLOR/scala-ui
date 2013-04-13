package ee.ui.display.traits

import org.specs2.mutable.Specification
import utils.MemberTypeTest
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.Property
import utils.SubtypeTest
import org.specs2.execute.Result

trait TraitTestTemplate extends DelayedInit { self: Specification =>

  xonly

  type PropertyType = (String, () => Result, () => Result, String, () => Result)

  def property(name: String, readOnlyMemberTest: => Result, memberTest: => Result, defaultValue: String, valueTest: => Result) =
    (name, () => readOnlyMemberTest, () => memberTest, defaultValue: String, () => valueTest)

  val name: String
  val properties: Seq[PropertyType]
  def subTypeTest(): Result

  def delayedInit(code: => Unit) = {
    code

    s"ReadOnly$name" should {
      properties foreach {
        case (property, readOnlyMemberTest, _, _, _) =>
          s"have a read only `$property` property with the correct signature" in {
            readOnlyMemberTest()
          }
      }
    }

    name should {
      s"extend ReadOnly$name" in {
        subTypeTest()
      }

      properties foreach {
        case (property, _, memberTest, defaultValue, valueTest) =>
          s"have a `$property` property with the correct signature" in {
            memberTest()
          }

          s"have a `$property` property with a default value of $defaultValue" in {
            valueTest()
          }
      }
    }
  }
}