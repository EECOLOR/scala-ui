package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Scene
import ee.ui.Node
import ee.ui.Group
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Scene
import ee.ui.Group
import ee.ui.nativeElements.Text
import ee.ui.nativeElements.Rectangle

object DummyNativeManager extends NativeManager {
  private def message(o:AnyRef, label:String, action:String = "update") =
    println(s"Dummy $label $action for $o")
  
  protected def register(o:Window):Unit = message(o, "window", "register")
    
  protected def update(o:Window):Unit = message(o, "window")
  protected def update(o:Scene):Unit = message(o, "scene")
  protected def update(o:Group):Unit = message(o, "group")
  protected def update(o:Text):Unit = message(o, "text")
  protected def update(o:Rectangle):Unit = message(o, "rectangle")
}

