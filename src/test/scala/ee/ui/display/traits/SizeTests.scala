package ee.ui.display.traits

import org.specs2.mutable.Specification
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest
import utils.TestUtils
import ee.ui.system.RestrictedAccess

object SizeTests extends Specification with TraitTestTemplate {

  val name: String = "Size"
  val instance = new Size {}

  def subTypeTest = SubtypeTest[Size <:< RestrictedSize]

  val properties = Seq(
    property(
      "width",
      SignatureTest[ReadOnlySize, ReadOnlyProperty[Double]](_.width),
      SignatureTest[Size, Property[Double]](_.width),
      "0",
      {
        val w = 1d
        instance.width.value === 0d
        instance.width = w
        instance.width.value === w
      }),
    property(
      "height",
      SignatureTest[ReadOnlySize, ReadOnlyProperty[Double]](_.height),
      SignatureTest[Size, Property[Double]](_.height),
      "0",
      {
        val h = 1d
        instance.height.value === 0d
        instance.height = h
        instance.height.value === h
      }))

  "Restricted size" should {

    "extend ReadOnlySize" in {
      SubtypeTest[RestrictedSize <:< ReadOnlySize]
    }

    "provide a setter for width and height with access restriction" in {
      SignatureTest[RestrictedSize, Double, Unit](_.width_=(_)(RestrictedAccess))
      SignatureTest[RestrictedSize, Double, Unit](_.height_=(_)(RestrictedAccess))
    }

    "set the correct sizes" in {
      implicit val r = RestrictedAccess
      val instance = new RestrictedSize {}
      val w = 1
      val h = 2
      instance.width = 1
      instance.height = 2
      instance.width.value === w
      instance.height.value === h
    }
  }
}
