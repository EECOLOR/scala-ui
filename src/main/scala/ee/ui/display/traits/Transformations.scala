package ee.ui.display.traits

import ee.ui.primitives.Transformation
import ee.ui.members.ObservableArrayBuffer

trait Transformations {
	val transformations = new ObservableArrayBuffer[Transformation]()
}