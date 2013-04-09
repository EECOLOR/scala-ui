package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess
import ee.ui.display.traits.Size
import ee.ui.display.traits.Title

class WindowTest extends Specification {

  xonly
  isolated

  val window = new Window

  "Window" should {

    "have an scene property with a default of None" in {
      val s1 = new Scene
      val s2 = new Scene
      window.scene.value === None
      window.scene = s1
      window.scene.value === Some(s1)
      window.scene = Some(s2)
      window.scene.value === Some(s2)
    }

    "have a showing property that is false by default" in {
      window.showing.value === false
    }

    "should be able to show and hide" in {
      var result = false
      window.showing.change { result = _ }

      Window.show(window)
      val shown = result === true

      Window.hide(window)
      val hidden = result === false

      shown and hidden
    }

    "have a title property with a default value of None" in {
      val t1 = "t1"
      val t2 = "t2"
      window.title.value === None
      window.title = t1
      window.title.value === Some(t1)
      window.title = Some(t2)
      window.title.value === Some(t2)
    }

    "have a size" in {
      window must beAnInstanceOf[Size with Title]
    }
  }

}