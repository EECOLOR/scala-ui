package ee.ui.display.traits

import org.specs2.mutable.Specification
import utils.MemberTypeTest
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.Property

class SizeTests extends Specification with TraitTestTemplate {

  val name: String = "Size"
  val instance = new Size {}

  def subTypeTest = instance must beAnInstanceOf[ReadOnlySize]

  val properties = Seq(
    property(
      "width",
      MemberTypeTest[ReadOnlySize, ReadOnlyProperty[Double]].forMember(_.width),
      MemberTypeTest[Size, Property[Double]].forMember(_.width),
      "0",
      {
        val w = 1d
        instance.width.value === 0d
        instance.width = w
        instance.width.value === w
      }),
    property(
      "height",
      MemberTypeTest[ReadOnlySize, ReadOnlyProperty[Double]].forMember(_.height),
      MemberTypeTest[Size, Property[Double]].forMember(_.height),
      "0",
      {
        val h = 1d
        instance.height.value === 0d
        instance.height = h
        instance.height.value === h
      }))
}
