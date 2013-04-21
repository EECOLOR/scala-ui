package ee.ui.display

import ee.ui.display.detail.ReadOnlyShape
import ee.ui.display.traits.Fill
import ee.ui.display.traits.Stroke

abstract class Shape extends Node with ReadOnlyShape with Fill with Stroke