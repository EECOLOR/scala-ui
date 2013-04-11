package ee.ui.display.detail

import ee.ui.display.traits.ReadOnlySize
import ee.ui.display.traits.ReadOnlyFill

sealed trait NodeContract { self: ReadOnlyNode => }

trait ReadOnlyNode extends ReadOnlySize
trait ReadOnlyRectangle extends NodeContract with ReadOnlyNode with ReadOnlyFill