package ee.ui.primitives.transformation

import ee.ui.primitives.Transformation
import ee.ui.primitives.Point

case class Scale(
  x: Double = 1,
  y: Double = 1,
  z: Double = 1,
  pivot: Point = Point.ZERO) extends Transformation {

  override val xx = x
  override val yy = y
  override val zz = z

  private val Point(pivotX, pivotY, pivotZ) = pivot

  override val xt = (1 - x) * pivotX
  override val yt = (1 - y) * pivotY
  override val zt = (1 - z) * pivotZ

  override lazy val isPureTranslation =
    xx == 1 && yy == 1 && zz == 1

  override lazy val isIdentity = isPureTranslation &&
    xt == 0 && yt == 0 && zt == 0

  override protected def transform(x: Double, y: Double, z: Double): Point =
    Point(
      xx * x + xt,
      yy * y + yt,
      zz * z + zt)

  override def ++(t: Transformation): Transformation = {

    val mxx = xx * t.xx
    val mxy = xx * t.xy
    val mxz = xx * t.xz
    val mxt = xx * t.xt + t.xt

    val myx = yy * t.yx
    val myy = yy * t.yy
    val myz = yy * t.yz
    val myt = yy * t.yt + t.yt

    val mzx = zz * t.zx
    val mzy = zz * t.zy
    val mzz = zz * t.zz
    val mzt = zz * t.zt + t.zt

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      mzx, mzy, mzz, mzt)
  }
}