package ee.ui.display.implementation.contracts

import ee.ui.display.detail.ReadOnlyNode
import ee.ui.display.shapes.detail.ReadOnlyRectangle

sealed trait NodeContract { self: ReadOnlyNode => }

trait RectangleContract extends NodeContract with ReadOnlyRectangle
