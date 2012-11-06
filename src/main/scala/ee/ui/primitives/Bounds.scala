package ee.ui.primitives

case class Bounds(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) {

  def this(minX: Double, minY: Double, maxX: Double, maxY: Double) =
    this(minX, minY, 0, maxX, maxY, 0)

  def transform(transformation: Transformation): Bounds = {

    @inline def t(x: Double, y: Double, z: Double): Point3D =
      transformation transform Point3D(x, y, z)

    val firstTransformation = t(maxX, maxY, maxZ)

    val transformations = Seq(
      t(minX, maxY, maxZ),
      t(minX, minY, maxZ),
      t(maxX, minY, maxZ),
      t(minX, maxY, minZ),
      t(maxX, maxY, minZ),
      t(minX, minY, minZ))

    val (minPoint, maxPoint) =
      (transformations foldLeft (firstTransformation -> firstTransformation)) {
        case ((minPoint, maxPoint), t) => (t min minPoint, t max maxPoint)
      }

    val Point3D(newMinX, newMinY, newMinZ) = minPoint
    val Point3D(newMaxX, newMaxY, newMaxZ) = maxPoint

    Bounds(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
  }
}

object Bounds extends ((Double, Double, Double, Double, Double, Double) => Bounds) {

  def apply(minX: Double, minY: Double, maxX: Double, maxY: Double) =
    new Bounds(minX, minY, maxX, maxY)

}