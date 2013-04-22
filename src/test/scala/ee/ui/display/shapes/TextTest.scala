package ee.ui.display.shapes

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.display.implementation.contracts.TextContract
import ee.ui.display.Node
import utils.SignatureTest
import ee.ui.members.Property
import ee.ui.display.shapes.detail.TextAlignment
import ee.ui.primitives.VerticalPosition

object TextTest extends Specification {

  xonly

  "Text" should {

    "have the correct types" in {
      SubtypeTest[Text <:< Node with TextContract]
    }

    "have a text property" in {
      SignatureTest[Text, Property[String]](_.text)
      val text = new Text
      text.text = "test1"
      text.text.value === "test1"
      text.text.value = "test2"
      text.text.value === "test2"
    }
    
    "have a textAlignment property" in {
      SignatureTest[Text, Property[TextAlignment]](_.textAlignment)
      val text = new Text
      text.textAlignment = TextAlignment.RIGHT
      text.textAlignment.value === TextAlignment.RIGHT
      text.textAlignment.value = TextAlignment.CENTER
      text.textAlignment.value === TextAlignment.CENTER
    }
    
    "have a textOrigin property" in {
      SignatureTest[Text, Property[VerticalPosition]](_.textOrigin)
      val text = new Text
      text.textOrigin = VerticalPosition.BOTTOM
      text.textOrigin.value === VerticalPosition.BOTTOM
      text.textOrigin.value = VerticalPosition.CENTER
      text.textOrigin.value === VerticalPosition.CENTER
    }
  }

}