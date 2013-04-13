package ee.ui.display.detail

import scala.annotation.implicitNotFound

import org.specs2.mutable.Specification

import ee.ui.display.traits.ReadOnlySize
import utils.SubtypeTest

object ReadOnlyNodeTest extends Specification {
  
  xonly
  
  "ReadOnlyNode" should {
    "have the correct type" in {
      SubtypeTest[ReadOnlyNode <:< ReadOnlySize]
    }
  }
}