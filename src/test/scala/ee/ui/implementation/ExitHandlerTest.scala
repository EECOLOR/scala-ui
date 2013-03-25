package ee.ui.implementation

import org.specs2.mutable.Specification
import ee.ui.application.TestApplication
import ee.ui.application.Application

class ExitHandlerTest extends Specification {
  xonly
  isolated
  
  "ExitHandler" should {
    "have an exit method" in {
      val application:Application = new TestApplication
      new ExitHandler {
        def exit(appliction:Application) = {}
      }.exit(application)
    }
  }
}