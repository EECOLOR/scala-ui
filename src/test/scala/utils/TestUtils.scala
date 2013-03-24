package utils

import scala.tools.reflect.ToolBox

object TestUtils {

  def eval(code: String, compileOptions: String = "-cp target/classes"): Any = {
    val tb = mkToolbox(compileOptions)
    tb.eval(tb.parse(code))
  }

  def mkToolbox(compileOptions: String = ""): ToolBox[_ <: scala.reflect.api.Universe] = {
    val m = scala.reflect.runtime.currentMirror
    m.mkToolBox(options = compileOptions)
  }
  
}