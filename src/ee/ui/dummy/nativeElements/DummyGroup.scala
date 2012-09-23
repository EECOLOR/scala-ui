package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeImplementation
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Scene
import ee.ui.Node
import ee.ui.Group

class DummyGroup(implemented:Group) extends NativeImplementation {
	def init = println(s"Dummy group ($this) initialized for $implemented")
}