package ee.ui.display.traits

import org.specs2.mutable.Specification

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest

object TitleTests extends Specification with TraitTestTemplate {

  val name: String = "Title"
  
  def subTypeTest = SubtypeTest[Title <:< ReadOnlyTitle]
  
  val properties = Seq(
    property(
      "title",
      SignatureTest[ReadOnlyTitle, ReadOnlyProperty[Option[String]]](_.title),
      SignatureTest[Title, Property[Option[String]]](_.title),
      "None",
      {
        val instance = new Title {}
        val t1 = "test1"
        val t2 = "test2"
        instance.title.value === None
        instance.title = t1
        instance.title.value === Some(t1)
        instance.title = Some(t2)
        instance.title.value === Some(t2)
      }))
}
