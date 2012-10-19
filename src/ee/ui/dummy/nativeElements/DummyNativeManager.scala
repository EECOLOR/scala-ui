package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Node
import ee.ui.Group
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Scene
import ee.ui.Group

object DummyNativeManager extends NativeManager {
  private def message(o:AnyRef, label:String) =
    println(s"Dummy $label initialized for $o")
  
  def update(o:Window):Unit = message(o, "window")
  def update(o:Stage):Unit = message(o, "stage")
  def update(o:Scene):Unit = message(o, "scene")
  def update(o:Group):Unit = message(o, "group")
}

