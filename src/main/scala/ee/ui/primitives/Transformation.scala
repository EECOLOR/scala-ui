package ee.ui.primitives

trait Transformation {

  val xx: Double = 1
  val xy: Double = 0
  val xz: Double = 0
  val xt: Double = 0

  val yx: Double = 0
  val yy: Double = 1
  val yz: Double = 0
  val yt: Double = 0

  val zx: Double = 0
  val zy: Double = 0
  val zz: Double = 1
  val zt: Double = 0

  lazy val isPureTranslation =
    xx == 1 && xy == 0 && xz == 0 &&
      yx == 0 && yy == 1 && yz == 0 &&
      zx == 0 && zy == 0 && zz == 1

  lazy val isIdentity =
    isPureTranslation && xt == 0 && yt == 0 && zt == 0

  def transform(p: Point): Point = {

    val Point(x, y, z) = p
    Point(
      xx * x + xy * y + xz * z + xt,
      yx * x + yy * y + yz * z + yt,
      zx * x + zy * y + zz * z + zt)
  }
}