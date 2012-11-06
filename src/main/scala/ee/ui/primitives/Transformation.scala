package ee.ui.primitives

sealed trait Transformation {
  
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

  def ++(t: Transformation) = {

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
}

case class Affine(
  override val xx: Double = 1,
  override val xy: Double = 0,
  override val xz: Double = 0,
  override val xt: Double = 0,

  override val yx: Double = 0,
  override val yy: Double = 1,
  override val yz: Double = 0,
  override val yt: Double = 0,

  override val zx: Double = 0,
  override val zy: Double = 0,
  override val zz: Double = 1,
  override val zt: Double = 0) extends Transformation {

}

case class Rotate(
  angle: Double = 0,
  axis: Point3D = Rotate.Z_AXIS,
  pivotX: Double = 0,
  pivotY: Double = 0,
  pivotZ: Double = 0) extends Transformation {

  val rad = math toRadians angle

  val factor = 1d / (math sqrt (axis.x * axis.x + axis.y * axis.y + axis.z * axis.z))

  val axisX = axis.x * factor
  val axisY = axis.y * factor
  val axisZ = axis.z * factor

  val sin = math sin rad
  val cos = math cos rad
  val `1 - cos` = 1.0D - cos

  val axisXZ = axisX * axisZ
  val axisXY = axisX * axisY
  val axisYZ = axisY * axisZ

  override val xx = `1 - cos` * axisX * axisX + cos
  override val xy = `1 - cos` * axisXY - (sin * axisZ)
  override val xz = `1 - cos` * axisXZ + sin * axisY
  override val xt = pivotX - pivotX * xx - pivotY * xy - pivotZ * xz

  override val yx = `1 - cos` * axisXY + sin * axisZ
  override val yy = `1 - cos` * axisY * axisY + cos
  override val yz = `1 - cos` * axisYZ - (sin * axisX)
  override val yt = pivotY - pivotX * yx - pivotY * yy - pivotZ * yz

  override val zx = `1 - cos` * axisXZ - (sin * axisY)
  override val zy = `1 - cos` * axisYZ + sin * axisX
  override val zz = `1 - cos` * axisZ * axisZ + cos
  override val zt = pivotZ - pivotX * zx - pivotY * zy - pivotZ * zz

}

object Rotate {
  val X_AXIS = Point3D(1, 0, 0)
  val Y_AXIS = Point3D(0, 1, 0)
  val Z_AXIS = Point3D(0, 0, 1)
}

case class Scale(
  x: Double = 0,
  y: Double = 0,
  z: Double = 0,
  pivotX: Double = 0,
  pivotY: Double = 0,
  pivotZ: Double = 0) extends Transformation {

  override val xx = x
  override val yy = y
  override val zz = z

  override val xt = (1 - x) * pivotX
  override val yt = (1 - y) * pivotY
  override val zt = (1 - z) * pivotZ
}

case class Shear(
  x: Double,
  y: Double,
  pivotX: Double,
  pivotY: Double) extends Transformation {

  override val xy = x
  override val yx = y

  override val xt = -x * pivotY
  override val yt = -y * pivotX
}

case class Translate(
  x: Double = 0,
  y: Double = 0,
  z: Double = 0) extends Transformation {

  override val xt = x
  override val yt = y
  override val zt = z

}
