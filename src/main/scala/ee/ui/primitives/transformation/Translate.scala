package ee.ui.primitives.transformation

import ee.ui.primitives.Transformation
import ee.ui.primitives.Point

case class Translate(
  x: Double = 0,
  y: Double = 0,
  z: Double = 0) extends Transformation {

  override val xt = x
  override val yt = y
  override val zt = z

  override lazy val isPureTranslation = true
  override lazy val isIdentity = xt == 0 && yt == 0 && zt == 0

  override protected def transform(x: Double, y: Double, z: Double): Point =
    Point(x + xt, y + yt, z + zt)

  override def ++(t: Transformation): Transformation = {

    val mxt = t.xx * xt + t.xy * yt + t.xz * zt + t.xt
    val myt = t.yx * xt + t.yy * yt + t.yz * zt + t.yt
    val mzt = t.zx * xt + t.zy * yt + t.zz * zt + t.zt

    Affine(
      t.xx, t.xy, t.xz, mxt,
      t.yx, t.yy, t.yz, myt,
      t.zx, t.zy, t.zz, mzt)
  }
}