package ee.ui.display.traits

import ee.ui.events.MouseButton.MIDDLE
import ee.ui.events.MouseButton.NONE
import ee.ui.events.MouseButton.PRIMARY
import ee.ui.events.MouseButton.SECONDARY
import ee.ui.events.MouseEvent
import ee.ui.members.Event
import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.details.Observable.ObservableExtensions

trait MouseTraits {
  //TODO add things like mouseChildren for groups and mouseEnabled

  private val writableHover = Property(false)
  val hover: ReadOnlyProperty[Boolean] = writableHover

  val onMouseOver = Event[MouseEvent]
  val onMouseOut = Event[MouseEvent]

  val onRollOver = Event[MouseEvent]
  val onRollOut = Event[MouseEvent]

  val onMouseDragOver = Event[MouseEvent]
  val onMouseDragOut = Event[MouseEvent]

  val onMouseDown = Event[MouseEvent]
  val onMouseUp = Event[MouseEvent]
  val onMouseUpOutside = Event[MouseEvent]
  val onMouseClicked = Event[MouseEvent]

  onMouseOver { writableHover.value = true }
  onMouseOut { writableHover.value = false }
}

trait ExtendedMouseTraits extends MouseTraits {
  val onClick = Event[MouseEvent]
  val onRightClick = Event[MouseEvent]
  val onMiddleClick = Event[MouseEvent]
  val onUnknownClick = Event[MouseEvent]

  val onDragOver = onMouseDragOver filter ( _.button == PRIMARY)
  val onDragOut = onMouseDragOut filter (_.button == PRIMARY)

  private val clickEvents = Map(
    PRIMARY -> onClick,
    SECONDARY -> onRightClick,
    MIDDLE -> onMiddleClick,
    NONE -> onUnknownClick)

  onMouseClicked { e => clickEvents(e.button) fire e }
  
}