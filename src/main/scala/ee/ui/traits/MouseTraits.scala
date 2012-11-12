package ee.ui.traits

import ee.ui.events.Event
import ee.ui.events.MouseEvent
import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty

trait MouseTraits {
	private val writableHover = new Property(false)
	val hover:ReadOnlyProperty[Boolean] = writableHover
  
	val onMouseOver = new Event[MouseEvent]
	val onMouseOut = new Event[MouseEvent]
	
	onMouseOver { writableHover.value = true }
	onMouseOut { writableHover.value = false }
}