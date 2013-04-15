package ee.ui.primitives.transformation

import ee.ui.primitives.Transformation
import ee.ui.primitives.Point

case class Rotate(
  angle: Double = 0,
  axis: Point = Rotate.Z_AXIS,
  pivot: Point = Point.ZERO) extends Transformation {
  
  private val factor =
    axis match {
      case Rotate.Z_AXIS | Rotate.Y_AXIS | Rotate.X_AXIS => 1
      case axis =>
        val squaredAxis = axis.x * axis.x + axis.y * axis.y + axis.z * axis.z

        require(squaredAxis != 0, s"Invalid axis $axis")
        
        1d / (math sqrt squaredAxis)
    }

  private val axisX = axis.x * factor
  private val axisY = axis.y * factor
  private val axisZ = axis.z * factor

  private val rad = math toRadians angle

  private val sin = math sin rad
  private val cos = math cos rad
  private val `1 - cos` = 1d - cos

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

  private val Point(pxt, pyt, pzt) = pivot

  // concatinate pivot translation to matrix (move to pivot)
  override val xt = xx * -pxt + xy * -pyt + xz * -pzt + pxt
  override val yt = yx * -pxt + yy * -pyt + yz * -pzt + pyt
  override val zt = zx * -pxt + zy * -pyt + zz * -pzt + pzt  
}

object Rotate {
  val X_AXIS = Point(1, 0, 0)
  val Y_AXIS = Point(0, 1, 0)
  val Z_AXIS = Point(0, 0, 1)
}