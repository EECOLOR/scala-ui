package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeImplementation
import ee.ui.nativeElements.Window

class DummyWindow(implemented:Window) extends NativeImplementation {
	def init = println(s"Dummy window ($this) initialized for $implemented")
}