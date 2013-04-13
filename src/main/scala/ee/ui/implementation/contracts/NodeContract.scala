package ee.ui.display.implementation.contracts

import ee.ui.display.shapes.detail.ReadOnlyRectangle
import ee.ui.display.detail.ReadOnlyNode

sealed trait NodeContract { self: ReadOnlyNode => }

trait RectangleContract extends NodeContract with ReadOnlyRectangle
