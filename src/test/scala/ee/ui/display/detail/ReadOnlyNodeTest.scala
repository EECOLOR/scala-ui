package ee.ui.display.detail

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.display.traits.ReadOnlySize

object ReadOnlyNodeTest extends Specification {
  "ReadOnlyNode" should {
    "have the correct type" in {
      SubtypeTest[ReadOnlyNode <:< ReadOnlySize]
    }
  }
}