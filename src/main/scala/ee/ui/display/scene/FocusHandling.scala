package ee.ui.display.scene

import ee.ui.display.Node
import ee.ui.display.Scene
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.details.Observable.ObservableExtensions
import ee.ui.system.RestrictedAccess

trait FocusHandling { self: Scene =>
  protected val writableFocusedNode = new Property[Option[Node]](None)
  val focusedNode: ReadOnlyProperty[Option[Node]] = writableFocusedNode
  
  focusedNode.valueChange collect {
    case (oldFocused, newFocused) => {
    	implicit val access = RestrictedAccess
    	oldFocused foreach (_.focused = false)
    	newFocused foreach (_.focused = true)
    }
  }
}