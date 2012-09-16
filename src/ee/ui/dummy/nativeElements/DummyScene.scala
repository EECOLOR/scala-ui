package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeImplementation
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Scene

class DummyScene(implemented:Scene) extends NativeImplementation {
	def init = println(s"Dummy scene ($this) initialized for $implemented")
}