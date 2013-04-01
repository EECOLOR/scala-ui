package ee.ui.implementation

import org.specs2.mutable.Specification
import ee.ui.application.Application

class EngineImplementationContractTest extends Specification {
  xonly
  isolated
  
  "EngineImplementationContract" should {
    "fullfill the dependencies of an application" in {
      new Application with EmptyEngineImplementationContract {
        def start(window: ee.ui.display.Window): Unit = ???
      }
      ok
    }
  }
}