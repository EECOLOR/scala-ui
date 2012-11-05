package ee.ui.traits

import ee.ui.primitives.Transform
import ee.ui.properties.ObservableArrayBuffer

trait Transforms {
	val transforms = new ObservableArrayBuffer[Transform]()
}