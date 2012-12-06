package ee.ui.primitives

case class Font(name: String, size: Option[Double] = None) {
  def this(name: String, size: Double) = this(name, Some(size))
}

object Font {
  object DEFAULT extends Font("System Regular")
}

case class FontMetrics(
  maxAscent: Float,
  ascent: Float,
  xheight: Float,
  descent: Float,
  maxDescent: Float,
  leading: Float,
  font: Font) {
  lazy val lineHeight = maxAscent + maxDescent + leading
}