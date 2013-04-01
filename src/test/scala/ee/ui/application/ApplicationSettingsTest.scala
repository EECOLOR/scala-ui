package ee.ui.application

import org.specs2.mutable.Specification
import utils.TypeTest
import ee.ui.members.Property
import ee.ui.members.ReadOnlyEvent
import scala.concurrent.Future
import scala.concurrent.promise

class ApplicationSettingsTest extends Specification {
  xonly
  isolated
  
  val applicationSettings = new ApplicationSettings(promise[Application].future)
  
  "ApplicationSettings" should {
    "have an implicitExit property" in {
      TypeTest[Property[Boolean]].forInstance(applicationSettings.implicitExit)
    }
    "be able to set the setting directly" in {
      val s = applicationSettings
      s.implicitExit = true
      ok
    }
    "have access to an application" in {
      new ApplicationSettings(applicationCreated = promise[Application].future)
      ok
    }
  }
}