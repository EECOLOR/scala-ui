package ee.ui.primitives

trait Transformation extends Equals {

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
    isPureTranslation &&
      xt == 0 && yt == 0 && zt == 0

  def transform(p: Point): Point = {

    val Point(x, y, z) = p
    Point(
      xx * x + xy * y + xz * z + xt,
      yx * x + yy * y + yz * z + yt,
      zx * x + zy * y + zz * z + zt)
  }

  /*
   * TODO this method is counter intuitive, 
   * it requires you to do translation before rotation instead of the other way around
   */
  def ++(t: Transformation): Transformation = {

    val mxx = xx * t.xx + xy * t.yx + xz * t.zx
    val mxy = xx * t.xy + xy * t.yy + xz * t.zy
    val mxz = xx * t.xz + xy * t.yz + xz * t.zz
    val mxt = xx * t.xt + xy * t.yt + xz * t.zt + xt

    val myx = yx * t.xx + yy * t.yx + yz * t.zx
    val myy = yx * t.xy + yy * t.yy + yz * t.zy
    val myz = yx * t.xz + yy * t.yz + yz * t.zz
    val myt = yx * t.xt + yy * t.yt + yz * t.zt + yt

    val mzx = zx * t.xx + zy * t.yx + zz * t.zx
    val mzy = zx * t.xy + zy * t.yy + zz * t.zy
    val mzz = zx * t.xz + zy * t.yz + zz * t.zz
    val mzt = zx * t.xt + zy * t.yt + zz * t.zt + zt

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      mzx, mzy, mzz, mzt)
  }
}

object Identity extends Transformation {
  override lazy val isPureTranslation = true
  override lazy val isIdentity = true

  override def transform(p: Point) = p
  override def ++(t: Transformation) = t
  
  def canEqual(that:Any) = that.isInstanceOf[Transformation]
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
  axis: Point = Rotate.Z_AXIS,
  pivot: Point = Point.ZERO) extends Transformation {

  val Point(pivotX, pivotY, pivotZ) = pivot

  private val rad = math toRadians angle

  private val factor =
    axis match {
      case Rotate.Z_AXIS | Rotate.Y_AXIS | Rotate.X_AXIS => 1
      case axis =>
        val squaredAxis = axis.x * axis.x + axis.y * axis.y + axis.z * axis.z

        if (squaredAxis == 0) throw new Error(s"Invalid axis $axis")
        else 1d / (math sqrt squaredAxis)
    }

  private val axisX = axis.x * factor
  private val axisY = axis.y * factor
  private val axisZ = axis.z * factor

  private val sin = math sin rad
  private val cos = math cos rad
  private val `1 - cos` = 1.0D - cos

  private val axisXZ = axisX * axisZ
  private val axisXY = axisX * axisY
  private val axisYZ = axisY * axisZ

  private val sinAxisX = axisX * sin
  private val sinAxisY = axisY * sin
  private val sinAxisZ = axisZ * sin

  //determine rotation matrix
  override val xx = cos + axisX * axisX * `1 - cos`
  override val xy = axisXY * `1 - cos` - sinAxisZ
  override val xz = axisXZ * `1 - cos` + sinAxisY

  override val yx = axisXY * `1 - cos` + sinAxisZ
  override val yy = cos + axisY * axisY * `1 - cos`
  override val yz = axisYZ * `1 - cos` - sinAxisX

  override val zx = axisXZ * `1 - cos` - sinAxisY
  override val zy = axisYZ * `1 - cos` + sinAxisX
  override val zz = cos + axisZ * axisZ * `1 - cos`

  // concatinate rotation to pivot translation (move to origin)
  private val pxt = pivotX
  private val pyt = pivotY
  private val pzt = pivotZ
  
  // concatinate pivot translation to matrix (move to pivot)
  override val xt = xx * -pivotX + xy * -pivotY + xz * -pivotZ + pxt
  override val yt = yx * -pivotX + yy * -pivotY + yz * -pivotZ + pyt
  override val zt = zx * -pivotX + zy * -pivotY + zz * -pivotZ + pzt 

}

object Rotate {
  val X_AXIS = Point(1, 0, 0)
  val Y_AXIS = Point(0, 1, 0)
  val Z_AXIS = Point(0, 0, 1)
}

case class Scale(
  x: Double = 0,
  y: Double = 0,
  z: Double = 0,
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

  override def transform(p: Point): Point = {

    val Point(x, y, z) = p
    Point(
      xx * x + xt,
      yy * y + yt,
      zz * z + zt)
  }

  override def ++(t: Transformation): Transformation = {

    val mxx = xx * t.xx
    val mxy = xx * t.xy
    val mxz = xx * t.xz
    val mxt = xx * t.xt + xt

    val myx = yy * t.yx
    val myy = yy * t.yy
    val myz = yy * t.yz
    val myt = yy * t.yt + yt

    val mzx = zz * t.zx
    val mzy = zz * t.zy
    val mzz = zz * t.zz
    val mzt = zz * t.zt + zt    

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      mzx, mzy, mzz, mzt)
  }

}

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

  override def transform(p: Point): Point = {

    val Point(x, y, z) = p
    Point(
      x + xy * y + xt,
      yx * x + y + yt,
      z)
  }

  override def ++(t: Transformation) = {

	val mxx = t.xx + xy * t.yx
    val mxy = t.xy + xy * t.yy
    val mxz = t.xz + xy * t.yz
    val mxt = t.xt + xy * t.yt + xt

    val myx = yx * t.xx + t.yx
    val myy = yx * t.xy + t.yy
    val myz = yx * t.xz + t.yz
    val myt = yx * t.xt + t.yt + yt

    Affine(
      mxx, mxy, mxz, mxt,
      myx, myy, myz, myt,
      t.zx, t.zy, t.zz, t.zt)
  }

}

case class Translate(
  x: Double = 0,
  y: Double = 0,
  z: Double = 0) extends Transformation {

  override val xt = x
  override val yt = y
  override val zt = z

  override lazy val isPureTranslation = true
  override lazy val isIdentity = xt == 0 && yt == 0 && zt == 0

  override def ++(t: Transformation) = {

    val mxt = t.xt + xt
    val myt = t.yt + yt
    val mzt = t.zt + zt
    
    Affine(
      t.xx, t.xy, t.xz, mxt,
      t.yx, t.yy, t.yz, myt,
      t.zx, t.zy, t.zz, mzt)
  }

  override def transform(p: Point): Point =
    Point(p.x + xt, p.y + yt, p.z + zt)

}
