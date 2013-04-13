package ee.ui.implementation

import org.specs2.mutable.Specification

import ee.ui.application.Application

object EngineImplementationContractTest extends Specification {
  xonly
  
  "EngineImplementationContract" should {
    
    "fullfill all the dependencies of an application (except for the start method)" in {
      new Application with EmptyEngineImplementationContract {
        def start(window: ee.ui.display.Window): Unit = ???
      }
      ok
    }
  }
}