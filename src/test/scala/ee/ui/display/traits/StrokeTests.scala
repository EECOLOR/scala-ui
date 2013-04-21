package ee.ui.display.traits

import org.specs2.mutable.Specification

import ee.ui.display.primitives.Color
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest

object StokeTests extends Specification with TraitTestTemplate {

  val name: String = "Stoke"
  
  def subTypeTest = SubtypeTest[Stroke <:< ReadOnlyStroke]
  
  val properties = Seq(
    property(
      "fill",
      SignatureTest[ReadOnlyStroke, ReadOnlyProperty[Option[Color]]](_.stroke),
      SignatureTest[Stroke, Property[Option[Color]]](_.stroke),
      "None",
      {
        val instance = new Stroke {}
        val c1 = Color(1)
        val c2 = Color(2)
        instance.stroke.value === None
        instance.stroke = c1
        instance.stroke.value === Some(c1)
        instance.stroke = Some(c2)
        instance.stroke.value === Some(c2)
      }))
}
