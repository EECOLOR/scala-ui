package ee.ui.primitives.transformation

import ee.ui.primitives.Transformation
import scala.runtime.Statics

case class Affine(
  override val xx: Double = 1,
  override val xy: Double = 0,
  override val xz: Double = 0,
  override val xt: Double = 0,

  override val yx: Double = 0,
  override val yy: Double = 1,
  override val yz: Double = 0,
  override val yt: Double = 0,

  override val zx: Double = 0,
  override val zy: Double = 0,
  override val zz: Double = 1,
  override val zt: Double = 0) extends Transformation {
}