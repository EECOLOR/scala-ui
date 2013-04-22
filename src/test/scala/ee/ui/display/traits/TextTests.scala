package ee.ui.display.traits

import org.specs2.mutable.Specification
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest
import utils.TestUtils
import ee.ui.system.RestrictedAccess

object TextTests extends Specification with TraitTestTemplate {

  val name: String = "Text"
  val instance = new Text {}

  def subTypeTest = SubtypeTest[Text <:< ReadOnlyText]

  val properties = Seq(
    property(
      "text",
      SignatureTest[ReadOnlyText, ReadOnlyProperty[String]](_.text),
      SignatureTest[Text, Property[String]](_.text),
      "''",
      {
        val t = "1"
        instance.text.value === ""
        instance.text = t
        instance.text.value === t
      }))
}
