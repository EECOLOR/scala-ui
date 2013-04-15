package ee.ui.display.traits

import org.specs2.mutable.Specification
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest
import utils.TestUtils
import ee.ui.system.RestrictedAccess

object PositionTests extends Specification with TraitTestTemplate {

  val name: String = "Position"
  val instance = new Position {}

  def subTypeTest = SubtypeTest[Position <:< RestrictedPosition]

  val properties = Seq(
    property(
      "x",
      SignatureTest[ReadOnlyPosition, ReadOnlyProperty[Double]](_.x),
      SignatureTest[Position, Property[Double]](_.x),
      "0",
      {
        val x = 1d
        instance.x.value === 0d
        instance.x = x
        instance.x.value === x
      }),
    property(
      "y",
      SignatureTest[ReadOnlyPosition, ReadOnlyProperty[Double]](_.y),
      SignatureTest[Position, Property[Double]](_.y),
      "0",
      {
        val y = 1d
        instance.y.value === 0d
        instance.y = y
        instance.y.value === y
      }))

  "Restricted position" should {

    "extend ReadOnlyPosition" in {
      SubtypeTest[RestrictedPosition <:< ReadOnlyPosition]
    }

    "provide a setter for x and y with access restriction" in {
      SignatureTest[RestrictedPosition, Double, Unit](_.x_=(_)(RestrictedAccess))
      SignatureTest[RestrictedPosition, Double, Unit](_.y_=(_)(RestrictedAccess))
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
