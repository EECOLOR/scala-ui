package ee.ui.display.implementation.contracts

import ee.ui.display.detail.ReadOnlyNode
import ee.ui.display.shapes.detail.ReadOnlyRectangle
import ee.ui.display.detail.ReadOnlyShape

sealed trait NodeContract extends ReadOnlyNode

sealed trait ShapeContract extends ReadOnlyShape { self: NodeContract => 

  val asNodeContract = self
}

trait RectangleContract extends NodeContract with ShapeContract with ReadOnlyRectangle
