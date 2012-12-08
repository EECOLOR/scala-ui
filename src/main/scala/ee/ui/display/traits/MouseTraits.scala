package ee.ui.display.traits

import ee.ui.events.Event
import ee.ui.events.MouseEvent
import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.events.MouseButton._
import scala.Predef.Map.apply

trait MouseTraits {
  //TODO add things like mouseChildren for groups and mouseEnabled

  private val writableHover = new Property(false)
  val hover: ReadOnlyProperty[Boolean] = writableHover

  val onMouseOver = new Event[MouseEvent]
  val onMouseOut = new Event[MouseEvent]

  val onRollOver = new Event[MouseEvent]
  val onRollOut = new Event[MouseEvent]

  val onMouseDragOver = new Event[MouseEvent]
  val onMouseDragOut = new Event[MouseEvent]

  val onMouseDown = new Event[MouseEvent]
  val onMouseUp = new Event[MouseEvent]
  val onMouseUpOutside = new Event[MouseEvent]
  val onMouseClicked = new Event[MouseEvent]

  onMouseOver { writableHover.value = true }
  onMouseOut { writableHover.value = false }
}

trait ExtendedMouseTraits extends MouseTraits {
  val onClick = new Event[MouseEvent]
  val onRightClick = new Event[MouseEvent]
  val onMiddleClick = new Event[MouseEvent]
  val onUnknownClick = new Event[MouseEvent]

  val onDragOver = new Event[MouseEvent]
  val onDragOut = new Event[MouseEvent]

  private val clickEvents = Map(
    PRIMARY -> onClick,
    SECONDARY -> onRightClick,
    MIDDLE -> onMiddleClick,
    NONE -> onUnknownClick)

  onMouseClicked { e => clickEvents(e.button) fire e }
  onMouseDragOver in { case e if (e.button == PRIMARY) => onDragOver fire e }
  onMouseDragOut in { case e if (e.button == PRIMARY) => onDragOut fire e }
}