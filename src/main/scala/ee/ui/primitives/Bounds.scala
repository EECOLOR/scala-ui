package ee.ui.primitives

//TODO create an optimized version for 2D
case class Bounds(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) {

  def this(minX: Double, minY: Double, maxX: Double, maxY: Double) =
    this(minX, minY, 0, maxX, maxY, 0)

  lazy val x = minX
  lazy val y = minY
  lazy val z = minZ

  lazy val width = maxX - minX
  lazy val height = maxY - minY
  lazy val depth = maxZ - minZ

  lazy val position = Point(x, y, z)
  lazy val size = Point(width, height, depth)

  def transform(transformation: Transformation): Bounds = {

    @inline def t(x: Double, y: Double, z: Double): Point =
      transformation transform Point(x, y, z)

    val firstTransformation = t(minX, minY, minZ)

    val transformations = Seq(
      t(minX, maxY, minZ),
      t(minX, maxY, maxZ),
      t(maxX, minY, minZ),
      t(maxX, minY, maxZ),
      t(maxX, maxY, minZ),
      t(maxX, maxY, maxZ))

    val (minPoint, maxPoint) =
      (transformations foldLeft (firstTransformation -> firstTransformation)) {
        case ((minPoint, maxPoint), t) => (t min minPoint, t max maxPoint)
      }

    val Point(newMinX, newMinY, newMinZ) = minPoint
    val Point(newMaxX, newMaxY, newMaxZ) = maxPoint

    Bounds(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
  }

  def contains(point: Point): Boolean = {
    val Point(x, y, z) = point
    x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ
  }

}

object Bounds {

  def apply(minX: Double, minY: Double, maxX: Double, maxY: Double): Bounds =
    new Bounds(minX, minY, maxX, maxY)

  def apply(min: Point, max: Point): Bounds =
    apply(min.x, min.y, min.z, max.x, max.y, max.z)

  def apply(point: Point): Bounds = {
    val zero = Point.ZERO
    val min = zero min point
    val max = zero max point

    apply(min, max)
  }

  val ZERO = new Bounds(0, 0, 0, 0)
}