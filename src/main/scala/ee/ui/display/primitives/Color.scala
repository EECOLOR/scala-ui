package ee.ui.display.primitives

case class Color(value: Int, alpha: Double = 1) {
  require(alpha >= 0, "Alpha should be larger than or equal to 0")
  require(alpha <= 1, "Alpha should be smaller than or equal to 1")
}

object Color {
  def BLACK = Color(0)
  def WHITE = Color(0xFFFFFF)
}