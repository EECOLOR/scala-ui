package ee.ui.display.scene

import ee.ui.display.Scene
import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.display.Node
import ee.ui.system.RestrictedAccess

trait FocusHandling { self: Scene =>
  protected val writableFocusedNode = new Property[Option[Node]](None)
  val focusedNode: ReadOnlyProperty[Option[Node]] = writableFocusedNode
  
  focusedNode.change in {
    case (oldFocused, newFocused) => {
    	implicit val access = RestrictedAccess
    	oldFocused foreach (_.focused = false)
    	newFocused foreach (_.focused = true)
    }
  }
}