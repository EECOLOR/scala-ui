package ee.ui.primitives

case class Point3D(x: Double, y: Double, z: Double) {

  def min(other: Point3D): Point3D =
    Point3D(
      math min (other.x, this.x),
      math min (other.y, this.y),
      math min (other.z, this.z))

  def max(other: Point3D): Point3D =
    Point3D(
      math max (other.x, this.x),
      math max (other.y, this.y),
      math max (other.z, this.z))
}