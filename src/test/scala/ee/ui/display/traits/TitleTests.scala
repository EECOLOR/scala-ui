package ee.ui.display.traits

import org.specs2.mutable.Specification
import utils.MemberTypeTest
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.Property

class TitleTests extends Specification with TraitTestTemplate {

  val name: String = "Title"
  val instance = new Title {}
  
  def subTypeTest = instance must beAnInstanceOf[ReadOnlyTitle]
  
  val properties = Seq(
    property(
      "title",
      MemberTypeTest[ReadOnlyTitle, ReadOnlyProperty[Option[String]]].forMember(_.title),
      MemberTypeTest[Title, Property[Option[String]]].forMember(_.title),
      "None",
      {
        val t1 = "test1"
        val t2 = "test2"
        instance.title.value === None
        instance.title = t1
        instance.title.value === Some(t1)
        instance.title = Some(t2)
        instance.title.value === Some(t2)
      }))
}
