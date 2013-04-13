package ee.ui.application

import org.specs2.mutable.Specification

import ee.ui.members.Property
import utils.SignatureTest

class ApplicationSettingsTest extends Specification {
  xonly
  isolated
  
  "ApplicationSettings" should {
    
    "have an implicitExit property" in {
      SignatureTest[ApplicationSettings, Property[Boolean]](_.implicitExit)
    }
    
    "be able to set the setting directly" in {
      val applicationSettings = new ApplicationSettings
      applicationSettings.implicitExit = true
      applicationSettings.implicitExit.value
    }
  }
}