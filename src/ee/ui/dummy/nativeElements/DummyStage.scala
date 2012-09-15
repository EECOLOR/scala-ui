package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeImplementation
import ee.ui.nativeElements.Stage

class DummyStage(implemented: Stage) extends DummyWindow(implemented) with NativeImplementation {
  override def init = {
    super.init
    println(s"Dummy stage ($this) initialized for $implemented")
  }
}