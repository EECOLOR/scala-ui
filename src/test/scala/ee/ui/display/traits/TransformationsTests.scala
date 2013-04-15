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
import ee.ui.members.ObservableSeq
import ee.ui.primitives.Transformation
import ee.ui.members.ObservableArrayBuffer

object TransformationsTests extends Specification with TraitTestTemplate {

  val name: String = "Transformations"
  val instance = new Transformations {}

  def subTypeTest = SubtypeTest[Transformations <:< ReadOnlyTransformations]

  val properties = Seq(
    property(
      "rotation",
      SignatureTest[ReadOnlyTransformations, ObservableSeq[Transformation]](_.transformations),
      SignatureTest[Transformations, ObservableArrayBuffer[Transformation]](_.transformations),
      "empty",
      {
        instance.transformations.isEmpty
      }))
}
