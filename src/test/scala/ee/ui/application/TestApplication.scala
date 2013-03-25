package ee.ui.application

import ee.ui.implementation.EmptyWindowImplementationHandler;
import ee.ui.implementation.EmptyWindowImplementationHandler

// introducing FakeEngine to show a potential bug in the DelayedInit trait 
trait FakeEngine {
  val windowImplementationHandler = EmptyWindowImplementationHandler
}

class TestApplication extends StubApplication with FakeEngine {
  // the body should be kept emtpy to make sure DelayedInit of Application is tested correctly
}