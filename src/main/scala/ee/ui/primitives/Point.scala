package ee.ui.primitives

case class Point(x: Double, y: Double, z: Double = 0) {
  
  def min(other: Point) =
    Point(
      math min (other.x, x),
      math min (other.y, y),
      math min (other.z, z))

  def max(other: Point) =
    Point(
      math max (other.x, x),
      math max (other.y, y),
      math max (other.z, z))

  def diff(other: Point) =
    Point(
      other.x - x,
      other.y - y,
      other.z - z)
}

object Point {

  def apply(x: Double, y: Double):Point =
    new Point(x, y, 0)
  
  val ZERO:Point = new Point(0, 0, 0)
}


