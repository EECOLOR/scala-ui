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

  private def transform(x: Double, y: Double, z: Double): Point =
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

    val mxx = xx * t.xx + xy * t.yx + xz * t.zx
    val mxy = xx * t.xy + xy * t.yy + xz * t.zy
    val mxz = xx * t.xz + xy * t.yz + xz * t.zz
    val mxt = xx * t.xt + xy * t.yt + xz * t.zt + t.xt

    val myx = yx * t.xx + yy * t.yx + yz * t.zx
    val myy = yx * t.xy + yy * t.yy + yz * t.zy
    val myz = yx * t.xz + yy * t.yz + yz * t.zz
    val myt = yx * t.xt + yy * t.yt + yz * t.zt + t.yt

    val mzx = zx * t.xx + zy * t.yx + zz * t.zx
    val mzy = zx * t.xy + zy * t.yy + zz * t.zy
    val mzz = zx * t.xz + zy * t.yz + zz * t.zz
    val mzt = zx * t.xt + zy * t.yt + zz * t.zt + t.zt

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      mzx, mzy, mzz, mzt)
  }
}