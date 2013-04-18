package ee.ui.display

import ee.ui.display.detail.ReadOnlyNode
import ee.ui.display.traits.CalculatedBounds
import ee.ui.display.traits.Rotation
import ee.ui.display.traits.Translation
import ee.ui.display.traits.Scaling
import ee.ui.display.traits.Transformations

abstract class Node extends ReadOnlyNode with Translation with Scaling with Rotation with Transformations