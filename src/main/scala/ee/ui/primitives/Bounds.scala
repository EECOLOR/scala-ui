package ee.ui.primitives

case class Bounds(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) {

  lazy val x = minX
  lazy val y = minY
  lazy val z = minZ

  lazy val width = maxX - minX
  lazy val height = maxY - minY
  lazy val depth = maxZ - minZ

  lazy val position = Point(x, y, z)
  lazy val size = Point(width, height, depth)

  def contains(point: Point): Boolean = {
    val Point(x, y, z) = point
    x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ
  }
}

object Bounds {
  
  def apply(minX: Double, minY: Double, maxX: Double, maxY: Double):Bounds =
    Bounds(minX, minY, 0, maxX, maxY, 0)

  def apply(min: Point, max: Point): Bounds =
    Bounds(min.x, min.y, min.z, max.x, max.y, max.z)

  def apply(point: Point): Bounds = {
    val zero = Point.ZERO
    val min = zero min point
    val max = zero max point

    apply(min, max)
  }
  
  val ZERO = Bounds(0, 0, 0, 0)
}