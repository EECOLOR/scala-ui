package ee.ui.display.traits

import org.specs2.mutable.Specification
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest
import utils.TestUtils
import ee.ui.system.RestrictedAccess
import ee.ui.primitives.Point
import ee.ui.primitives.transformation.Rotate

object RotationTests extends Specification with TraitTestTemplate {

  val name: String = "Rotation"
  val instance = new Rotation {}

  def subTypeTest = SubtypeTest[Rotation <:< ReadOnlyRotation]

  val properties = Seq(
    property(
      "rotation",
      SignatureTest[ReadOnlyRotation, ReadOnlyProperty[Double]](_.rotation),
      SignatureTest[Rotation, Property[Double]](_.rotation),
      "0",
      {
        val r = 1d
        instance.rotation.value === 0d
        instance.rotation = r
        instance.rotation.value === r
      }),
    property(
      "rotationAxis",
      SignatureTest[ReadOnlyRotation, ReadOnlyProperty[Point]](_.rotationAxis),
      SignatureTest[Rotation, Property[Point]](_.rotationAxis),
      "Rotate.Z_AXIS",
      {
        val a = Point(1, 2, 3)
        instance.rotationAxis.value === Rotate.Z_AXIS
        instance.rotationAxis = a
        instance.rotationAxis.value === a
      }))
}
