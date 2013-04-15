package ee.ui.display.traits

import org.specs2.mutable.Specification
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest
import utils.TestUtils
import ee.ui.system.RestrictedAccess

object ScalingTests extends Specification with TraitTestTemplate {

  val name: String = "Scaling"
  val instance = new Scaling {}

  def subTypeTest = SubtypeTest[Scaling <:< ReadOnlyScaling]

  val properties = Seq(
    property(
      "scaleX",
      SignatureTest[ReadOnlyScaling, ReadOnlyProperty[Double]](_.scaleX),
      SignatureTest[Scaling, Property[Double]](_.scaleX),
      "1",
      {
        val x = 2d
        instance.scaleX.value === 1d
        instance.scaleX = x
        instance.scaleX.value === x
      }),
    property(
      "scaleY",
      SignatureTest[ReadOnlyScaling, ReadOnlyProperty[Double]](_.scaleY),
      SignatureTest[Scaling, Property[Double]](_.scaleY),
      "1",
      {
        val y = 2d
        instance.scaleY.value === 1d
        instance.scaleY = y
        instance.scaleY.value === y
      }),
    property(
      "scaleZ",
      SignatureTest[ReadOnlyScaling, ReadOnlyProperty[Double]](_.scaleZ),
      SignatureTest[Scaling, Property[Double]](_.scaleZ),
      "1",
      {
        val z = 2d
        instance.scaleZ.value === 1d
        instance.scaleZ = z
        instance.scaleZ.value === z
      }))
}
