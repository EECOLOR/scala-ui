package ee.ui.primitives

import org.specs2.mutable.Specification
import utils.SignatureTest

object BoundsTest extends Specification {

  xonly

  val bounds = Bounds(-1, -2, -3, 1, 2, 3)

  "Bounds" should {

    "have an alternative constructor with z properties 0" in {

      val Bounds(minX, minY, minZ, maxX, maxY, maxZ) = Bounds(1, 2, 3, 4)

      minX === 1
      minY === 2
      minZ === 0
      maxX === 3
      maxY === 4
      maxZ === 0
    }

    "have a properties x, y and z" in {
      bounds.x === -1
      bounds.y === -2
      bounds.z === -3
    }

    "have a properties width, height and depth" in {
      bounds.width === 2
      bounds.height === 4
      bounds.depth === 6
    }

    "have a position property" in {
      bounds.position === Point(-1, -2, -3)
    }

    "have a size property" in {
      bounds.size === Point(2, 4, 6)
    }

    "have a contains method" in {
      bounds.contains(Point(-4, 0, 0)) === false
      bounds.contains(Point(0, -4, 0)) === false
      bounds.contains(Point(0, 0, -4)) === false
      bounds.contains(Point(4, 0, 0)) === false
      bounds.contains(Point(0, 4, 0)) === false
      bounds.contains(Point(0, 0, 4)) === false
      bounds.contains(Point(0, 0, 0)) === true
    }

    "be created from two points" in {
      Bounds(Point(-1, -2, -3), Point(1, 2, 3)) === bounds
    }

    "be created in relation to the zero point" in {
      Bounds(Point(-1, -2, -3)) === Bounds(-1, -2, -3, 0, 0, 0)
      Bounds(Point(1, 2, 3)) === Bounds(0, 0, 0, 1, 2, 3)
    }
    
    "have a ZERO instance" in {
      SignatureTest[Bounds.type, Bounds](_.ZERO)
      Bounds.ZERO === Bounds(0, 0, 0, 0)
    }
  }
}