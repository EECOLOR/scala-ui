package ee.ui.display.traits

import org.specs2.mutable.Specification

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import utils.SignatureTest
import utils.SubtypeTest

object SizeTests extends Specification with TraitTestTemplate {

  val name: String = "Size"
  val instance = new Size {}

  def subTypeTest = SubtypeTest[Size <:< ReadOnlySize]

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
}
