package ee.ui.primitives

//mxx, mxy, mxz, myx, myy, myz, mzx, mzy, mzz, tx, ty, tz
sealed trait Transform {
  val mxx: Double = 1
  val mxy: Double = 0
  val mxz: Double = 0

  val myx: Double = 0
  val myy: Double = 1
  val myz: Double = 0

  val mzx: Double = 0
  val mzy: Double = 0
  val mzz: Double = 1

  val tx: Double = 0
  val ty: Double = 0
  val tz: Double = 0
  
}

case class Affine(
  override val mxx: Double = 1,
  override val mxy: Double = 0,
  override val mxz: Double = 0,

  override val myx: Double = 0,
  override val myy: Double = 1,
  override val myz: Double = 0,

  override val mzx: Double = 0,
  override val mzy: Double = 0,
  override val mzz: Double = 1,

  override val tx: Double = 0,
  override val ty: Double = 0,
  override val tz: Double = 0) extends Transform

case class Rotate(
  angle: Double = 0,
  axis: Point3D = Rotate.Z_AXIS,
  pivotX: Double = 0,
  pivotY: Double = 0,
  pivotZ: Double = 0) extends Transform {

  private val cos = math cos (math toRadians angle)
  private val sin = math sin (math toRadians angle)

  private val normalizedAxis = {
    val mag = math sqrt (axis.x * axis.x + axis.y * axis.y + axis.z * axis.z)
    if (mag == 0) Rotate.Z_AXIS
    else Point3D(axis.x / mag, axis.y / mag, axis.z / mag)
  }

  override val mxx = {
    val axisX = normalizedAxis.x
    cos + axisX * axisX * (1 - cos);
  }

  override val mxy =
    normalizedAxis.x * normalizedAxis.y * (1 - cos) - normalizedAxis.z * sin

  override val mxz =
    normalizedAxis.x * normalizedAxis.z * (1 - cos) + normalizedAxis.y * sin

  override val tx =
    pivotX -
      pivotX * mxx -
      pivotY * mxy -
      pivotZ * mxz

  override val myx =
    normalizedAxis.y * normalizedAxis.x * (1 - cos) + normalizedAxis.z * sin

  override val myy = {
    val axisY = normalizedAxis.y
    cos + axisY * axisY * (1 - cos)
  }

  override val myz =
    normalizedAxis.y * normalizedAxis.z * (1 - cos) - normalizedAxis.x * sin

  override val ty =
    pivotY -
      pivotX * myx -
      pivotY * myy -
      pivotZ * myz

  override val mzx =
    normalizedAxis.z * normalizedAxis.x * (1 - cos) - normalizedAxis.y * sin

  override val mzy =
    normalizedAxis.z * normalizedAxis.y * (1 - cos) + normalizedAxis.x * sin

  override val mzz = {
    val axisZ = normalizedAxis.z
    cos + axisZ * axisZ * (1 - cos)
  }

  override val tz =
    pivotZ -
      pivotX * mzx -
      pivotY * mzy -
      pivotZ * mzz
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
  pivotZ: Double = 0) extends Transform {

  override val mxx = x
  override val myy = y
  override val mzz = z

  override val tx = (1 - x) * pivotX
  override val ty = (1 - y) * pivotY
  override val tz = (1 - z) * pivotZ
}

case class Shear(
  x: Double,
  y: Double,
  pivotX: Double,
  pivotY: Double) extends Transform {

  override val mxy = x
  override val myx = y

  override val tx = -x * pivotY
  override val ty = -y * pivotX
}

case class Translate(
  x: Double = 0,
  y: Double = 0,
  z: Double = 0) extends Transform {

  override val tx = x
  override val ty = y
  override val tz = z
}
