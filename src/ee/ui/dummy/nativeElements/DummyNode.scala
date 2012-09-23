package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeImplementation
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Scene
import ee.ui.Node

class DummyNode(implemented:Node) extends NativeImplementation {
	def init = println(s"Dummy node ($this) initialized for $implemented")
}