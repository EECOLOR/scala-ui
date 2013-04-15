package ee.ui.primitives

import ee.ui.primitives.transformation.Affine

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
    transform(x, y, z)
  }

  protected def transform(x: Double, y: Double, z: Double): Point =
    Point(
      xx * x + xy * y + xz * z + xt,
      yx * x + yy * y + yz * z + yt,
      zx * x + zy * y + zz * z + zt)

  def transform(b: Bounds): Bounds = {

    val Bounds(minX, minY, minZ, maxX, maxY, maxZ) = b

    val transformations = Seq(
      transform(minX, minY, minZ),
      transform(minX, minY, maxZ),
      transform(minX, maxY, minZ),
      transform(minX, maxY, maxZ),
      transform(maxX, minY, minZ),
      transform(maxX, minY, maxZ),
      transform(maxX, maxY, minZ),
      transform(maxX, maxY, maxZ))

    val head = transformations.head
    val tail = transformations.tail

    val (minPoint, maxPoint) =
      tail.foldLeft(head -> head) {
        case ((minPoint, maxPoint), p) => (p min minPoint, p max maxPoint)
      }

    Bounds(minPoint, maxPoint)
  }

  def ++(t: Transformation): Transformation = {

    val mxx = t.xx * xx + t.xy * yx + t.xz * zx
    val mxy = t.xx * xy + t.xy * yy + t.xz * zy
    val mxz = t.xx * xz + t.xy * yz + t.xz * zz
    val mxt = t.xx * xt + t.xy * yt + t.xz * zt + t.xt

    val myx = t.yx * xx + t.yy * yx + t.yz * zx
    val myy = t.yx * xy + t.yy * yy + t.yz * zy
    val myz = t.yx * xz + t.yy * yz + t.yz * zz
    val myt = t.yx * xt + t.yy * yt + t.yz * zt + t.yt

    val mzx = t.zx * xx + t.zy * yx + t.zz * zx
    val mzy = t.zx * xy + t.zy * yy + t.zz * zy
    val mzz = t.zx * xz + t.zy * yz + t.zz * zz
    val mzt = t.zx * xt + t.zy * yt + t.zz * zt + t.zt

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      mzx, mzy, mzz, mzt)
  }

  def toAffine =
    if (isInstanceOf[Affine]) asInstanceOf[Affine]
    else Affine(xx, xy, xz, xt, yx, yy, yz, yt, zx, zy, zz, zt)
}