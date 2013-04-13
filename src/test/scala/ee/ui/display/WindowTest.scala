package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.display.detail.ReadOnlyScene
import ee.ui.display.traits.Size
import ee.ui.display.traits.Title
import ee.ui.implementation.contracts.SceneContract
import ee.ui.members.Property
import utils.SignatureTest
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess
import utils.SubtypeTest

class WindowTest extends Specification {

  xonly
  isolated

  val window = new Window

  val scene = new Scene
  def correctSceneValue = window.scene.value === Some(scene)

  val title = "title"
  def correctTitleValue = window.title.value === Some(title)

  "Window" should {

    "extend the correct traits" in {
      SubtypeTest[Window <:< Size with Title with ReadOnlyScene]
    }
    
    "have a scene property" >> {

      "with the correct signature" in {
        SignatureTest[Window, Property[Option[SceneContract]]](_.scene)
      }

      "with a default value of None" in {
        window.scene.value === None
      }

      "that can be set directly" in {
        window.scene = scene
        correctSceneValue
      }

      "that can be set as an option" in {
        window.scene = Some(scene)
        correctSceneValue
      }
    }

    "have a showing property" >> {

      "with the correct signature" in {
        SignatureTest[Window, ReadOnlyProperty[Boolean]](_.showing)
      }

      "that is false by default" in {
        window.showing.value === false
      }

      "that is set on show and hide" in {
        implicit val restrictedAccess = RestrictedAccess

        var result = false
        window.showing.change { result = _ }

        Window.show(window)
        val shown = result

        Window.hide(window)
        val hidden = !result

        shown and hidden
      }
    }

    "have a title property" >> {

      "with the correct signature" in {
        SignatureTest[Window, Property[Option[String]]](_.title)
      }

      "with a default value of None" in {
        window.title.value === None
      }

      "that can be set directly" in {
        window.title = title
        correctTitleValue
      }

      "that can be set as an option" in {
        window.title = Some(title)
        correctTitleValue
      }
    }
  }
}