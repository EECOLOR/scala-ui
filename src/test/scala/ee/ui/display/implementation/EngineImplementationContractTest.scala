package ee.ui.display.implementation

import org.specs2.mutable.Specification

class EngineImplementationContractTest extends Specification {
  xonly
  isolated
  
  "EngineImplementationContract" should {
    "have a window implementation handler" in {
      def x(y:EngineImplementationContract) = y.windowImplementationHandler
    }
  }
}