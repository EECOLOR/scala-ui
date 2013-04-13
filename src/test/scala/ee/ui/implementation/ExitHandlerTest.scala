package ee.ui.implementation

import org.specs2.mutable.Specification
import ee.ui.application.TestApplication
import ee.ui.application.Application
import utils.SignatureTest
import ee.ui.application.Application

object ExitHandlerTest extends Specification {

  xonly
  
  "ExitHandler" should {
    
    "have an exit method" in {
      SignatureTest[ExitHandler, Application, Unit](_.exit(_))
    }
  }
}