package ee.ui.display.traits

import org.specs2.mutable.Specification
import utils.MemberTypeTest
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.Property
import ee.ui.display.primitives.Color

class FillTests extends Specification with TraitTestTemplate {

  val name: String = "Fill"
  val instance = new Fill {}
  
  def subTypeTest = instance must beAnInstanceOf[ReadOnlyFill]
  
  val properties = Seq(
    property(
      "fill",
      MemberTypeTest[ReadOnlyFill, ReadOnlyProperty[Option[Color]]].forMember(_.fill),
      MemberTypeTest[Fill, Property[Option[Color]]].forMember(_.fill),
      "None",
      {
        val c1 = Color(1)
        val c2 = Color(2)
        instance.fill.value === None
        instance.fill = c1
        instance.fill.value === Some(c1)
        instance.fill = Some(c2)
        instance.fill.value === Some(c2)
      }))
}
