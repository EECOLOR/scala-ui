package ee.ui.primitives

import org.specs2.mutable.Specification

object PointTest extends Specification {

  xonly

  val point1 = Point(1, 4, 5)
  val point2 = Point(2, 3, 6)

  "Point" should {

    "have a default value of 0 for z" in {
      val Point(x, y, z) = Point(1, 2)
      x === 1
      y === 2
      z === 0
    }

    "have a min method" in {
      (point1 min point2) === Point(1, 3, 5)
      (point2 min point1) === Point(1, 3, 5)
    }

    "have a max method" in {
      (point1 max point2) === Point(2, 4, 6)
      (point2 max point1) === Point(2, 4, 6)
    }
    
    "have a diff method, other - this" in {
      (point1 diff point2) === Point(1, -1, 1)
      (point2 diff point1) === Point(-1, 1, -1)
    }
    
    "have a ZERO instance" in {
      Point.ZERO === Point(0, 0, 0)
    }
  }

}