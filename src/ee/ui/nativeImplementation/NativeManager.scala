package ee.ui.nativeImplementation

import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Group

trait NativeManager {
  def update(o:Stage):Unit
  def update(o:Scene):Unit
  def update(o:Group):Unit
}
