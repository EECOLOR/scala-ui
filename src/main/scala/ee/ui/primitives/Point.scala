package ee.ui.primitives

case class Point(x: Double, y: Double, z: Double = 0) {

  def min(other: Point) =
    Point(
      other.x min x,
      other.y min y,
      other.z min z)

  def max(other: Point) =
    Point(
      other.x max x,
      other.y max y,
      other.z max z)

  def diff(other: Point) =
    Point(
      other.x - x,
      other.y - y,
      other.z - z)
}

object Point {
  
  val ZERO = Point(0, 0, 0)
}

