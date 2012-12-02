package ee.ui.nativeElements.scene

import ee.ui.nativeElements.Scene
import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.Node

trait FocusHandling { self: Scene =>
  protected val writableFocusedNode = new Property[Option[Node]](None)
  val focusedNode: ReadOnlyProperty[Option[Node]] = writableFocusedNode
  
  focusedNode onChangedIn {
    case (oldFocused, newFocused) => {
    	oldFocused foreach (_.focused = false)
    	newFocused foreach (_.focused = true)
    }
  }
}