package ee.ui.display.shapes.detail

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.display.shapes.detail.{ReadOnlyText => ReadOnlyTextTrait}
import ee.ui.display.detail.ReadOnlyShape
import utils.SignatureTest
import ee.ui.members.ReadOnlyProperty
import ee.ui.primitives.VerticalPosition


object ReadOnlyTextTest extends Specification {

  xonly
  
  "ReadOnlyText" should {
    
    "have the correct type" in {
      SubtypeTest[ReadOnlyText <:< ReadOnlyShape]
    }
    
    "have a text property" in {
      SignatureTest[ReadOnlyText, ReadOnlyProperty[String]](_.text)
    }
    
    "have a textAlignment property with a default value of LEFT" in {
      SignatureTest[ReadOnlyText, ReadOnlyProperty[TextAlignment]](_.textAlignment)
      val text = new ReadOnlyText {}
      text.textAlignment.value === TextAlignment.LEFT
    }
    
    "have a textOrigin property with a default value of TOP" in {
      SignatureTest[ReadOnlyText, ReadOnlyProperty[VerticalPosition]](_.textOrigin)
      val text = new ReadOnlyText {}
      text.textOrigin.value === VerticalPosition.TOP
    }
  }
  
}