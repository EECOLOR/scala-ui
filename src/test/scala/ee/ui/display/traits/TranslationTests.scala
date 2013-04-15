package ee.ui.display.traits

import org.specs2.mutable.Specification
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest
import utils.TestUtils
import ee.ui.system.RestrictedAccess

object TranslationTests extends Specification with TraitTestTemplate {

  val name: String = "Translation"
  val instance = new Translation {}

  def subTypeTest = SubtypeTest[Translation <:< ReadOnlyTranslation]

  val properties = Seq(
    property(
      "translateX",
      SignatureTest[ReadOnlyTranslation, ReadOnlyProperty[Double]](_.translateX),
      SignatureTest[Translation, Property[Double]](_.translateX),
      "0",
      {
        val x = 1d
        instance.translateX.value === 0d
        instance.translateX = x
        instance.translateX.value === x
      }),
    property(
      "translateY",
      SignatureTest[ReadOnlyTranslation, ReadOnlyProperty[Double]](_.translateY),
      SignatureTest[Translation, Property[Double]](_.translateY),
      "0",
      {
        val y = 1d
        instance.translateY.value === 0d
        instance.translateY = y
        instance.translateY.value === y
      }),
    property(
      "translateZ",
      SignatureTest[ReadOnlyTranslation, ReadOnlyProperty[Double]](_.translateZ),
      SignatureTest[Translation, Property[Double]](_.translateZ),
      "0",
      {
        val z = 1d
        instance.translateZ.value === 0d
        instance.translateZ = z
        instance.translateZ.value === z
      }))
}
