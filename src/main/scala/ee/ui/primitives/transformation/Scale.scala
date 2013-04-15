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

    val mxx = t.xx * xx
    val mxy = t.xy * yy
    val mxz = t.xz * zz
    val mxt = t.xx * xt + t.xy * yt + t.xz * zt + t.xt

    val myx = t.yx * xx
    val myy = t.yy * yy
    val myz = t.yz * zz
    val myt = t.yx * xt + t.yy * yt + t.yz * zt + t.yt

    val mzx = t.zx * xx
    val mzy = t.zy * yy
    val mzz = t.zz * zz
    val mzt = t.zx * xt + t.zy * yt + t.zz * zt + t.zt

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      mzx, mzy, mzz, mzt)
  }
}