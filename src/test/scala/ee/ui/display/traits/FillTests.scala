package ee.ui.display.traits

import org.specs2.mutable.Specification

import ee.ui.display.primitives.Color
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest

object FillTests extends Specification with TraitTestTemplate {

  val name: String = "Fill"
  
  def subTypeTest = SubtypeTest[Fill <:< ReadOnlyFill]
  
  val properties = Seq(
    property(
      "fill",
      SignatureTest[ReadOnlyFill, ReadOnlyProperty[Option[Color]]](_.fill),
      SignatureTest[Fill, Property[Option[Color]]](_.fill),
      "None",
      {
        val instance = new Fill {}
        val c1 = Color(1)
        val c2 = Color(2)
        instance.fill.value === None
        instance.fill = c1
        instance.fill.value === Some(c1)
        instance.fill = Some(c2)
        instance.fill.value === Some(c2)
      }))
}
