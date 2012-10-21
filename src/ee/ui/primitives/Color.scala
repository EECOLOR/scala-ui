package ee.ui.primitives

class Color(val value:Int, val alpha:Double = 1) extends Paint {
  require(alpha >= 0, "Alpha should be larger than or equal to 0")
  require(alpha <= 1, "Alpha should be smaller than or equal to 1")
}

object Color {
  def apply(value:Int, alpha:Double = 1) = new Color(value, alpha)
  
  object WHITE extends Color(0xFFFFFF) 
  object BLACK extends Color(0x000000) 
}