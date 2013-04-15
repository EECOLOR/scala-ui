package ee.ui.primitives.transformation

import ee.ui.primitives.Transformation
import ee.ui.primitives.Point

case class Shear(
  x: Double = 0,
  y: Double = 0,
  pivot: Point = Point.ZERO) extends Transformation {

  override val xy = x
  override val yx = y

  private val Point(pivotX, pivotY, _) = pivot

  override val xt = -x * pivotY
  override val yt = -y * pivotX

  override lazy val isPureTranslation = xy == 0 && yx == 0
  override lazy val isIdentity = isPureTranslation

  override protected def transform(x: Double, y: Double, z: Double): Point =
    Point(
      x + xy * y + xt,
      yx * x + y + yt,
      z)

  override def ++(t: Transformation) = {

    val mxx = t.xx + t.xy * yx
    val mxy = t.xx * xy + t.xy
    val mxz = t.xz
    val mxt = t.xx * xt + t.xy * yt + t.xt

    val myx = t.yx + t.yy * yx
    val myy = t.yx * xy + t.yy
    val myz = t.yz
    val myt = t.yx * xt + t.yy * yt + t.yt

    val mzx = t.zx + t.zy * yx
    val mzy = t.zx * xy + t.zy
    val mzz = t.zz
    val mzt = t.zx * xt + t.zy * yt + t.zt

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      mzx, mzy, mzz, mzt)
  }
}